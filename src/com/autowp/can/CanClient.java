/**
 * 
 */
package com.autowp.can;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.autowp.can.CanAdapter.CanFrameEvent;


/**
 * @author Dmitry
 *
 */
public class CanClient {
    protected CanAdapter adapter;
    
    private CanBusSpecs specs;
    
    private static final int PCITYPE_SINGLE_FRAME = 0;
    private static final int PCITYPE_FIRST_FRAME = 1;
    private static final int PCITYPE_CONSECUTIVE_FRAME = 2;
    private static final int PCITYPE_FLOW_CONTROL_FRAME = 3;
    
    private List<Timer> timers = new ArrayList<Timer>();
    
    public interface OnClientConnectedStateChangeListener {
        void handleClientConnectedStateChanged(boolean isConnected);
    }
    
    public interface OnCanFrameTransferListener {
        public void handleCanFrameReceivedEvent(CanFrame frame);
        
        public void handleCanFrameSentEvent(CanFrame frame);
    }
    
    public interface OnCanMessageTransferListener {
        public void handleCanMessageReceivedEvent(CanMessage message);
        
        public void handleCanMessageSentEvent(CanMessage message);
    }
    
    public interface OnCanClientErrorListener {
        public void handleErrorEvent(CanClientException e);
    }
    
    private List<OnClientConnectedStateChangeListener> mClientConnectedStateChangeListeners = 
            new ArrayList<OnClientConnectedStateChangeListener>();
    
    private List<OnCanFrameTransferListener> mCanFrameTransferListeners = 
            new ArrayList<OnCanFrameTransferListener>();
    
    private List<OnCanMessageTransferListener> mCanMessageTransferListeners = 
            new ArrayList<OnCanMessageTransferListener>();
    
    private List<OnCanClientErrorListener> mErrorListeners = 
            new ArrayList<OnCanClientErrorListener>();
    
    private CanAdapter.CanAdapterEventListener mCanFrameEventClassListener = 
            new CanAdapter.CanAdapterEventListener() {

        HashMap<Integer, MultiFrameBuffer> mMultiframeBuffers = new HashMap<Integer, MultiFrameBuffer>();
        
        public void handleCanFrameSentEvent(CanFrameEvent e) {
            CanFrame frame = e.getFrame();
            fireCanFrameSentEvent(frame);
        }
        
        public void handleCanFrameReceivedEvent(CanFrameEvent e) {
            CanFrame frame = e.getFrame();
            fireCanFrameReceivedEvent(frame);
            
            try {
                int arbID = frame.getId();
                // check is multiFrame
                if (specs.isMultiFrame(arbID)) { 
                    
                    byte[] data = frame.getData();
                    if (data.length <= 0) {
                        throw new CanClientException("Unexpected zero size can message");
                    }
                    
                    int pciType = (data[0] & 0xF0) >>> 4;
                    
                    switch (pciType) {
                        case PCITYPE_SINGLE_FRAME: {
                            int dataLength = data[0] & 0x0F;
                            byte[] messageData = new byte[dataLength];
                            System.arraycopy(data, 1, messageData, 0, dataLength);
                            fireCanMessageReceivedEvent(
                                new CanMessage(arbID, messageData)
                            );
                            break;
                        }
                            
                        case PCITYPE_FIRST_FRAME: {
                            int dataLengthHigh = data[0] & 0x0F;
                            int dataLengthLow = (int) data[1] & 0xFF;
                            
                            int dataLength = (dataLengthHigh << 8) + dataLengthLow;
                            
                            byte[] messageData = new byte[data.length - 2];
                            System.arraycopy(data, 2, messageData, 0, data.length - 2);
                            
                            MultiFrameBuffer buffer = new MultiFrameBuffer(dataLength);
                            buffer.append(messageData, 0);
                            
                            mMultiframeBuffers.put(arbID, buffer);
                            break;
                        }
                            
                        case PCITYPE_CONSECUTIVE_FRAME: {
                           
                            int index = data[0] & 0x0F;
                            byte[] messageData = new byte[data.length - 1];
                            System.arraycopy(data, 1, messageData, 0, data.length - 1);
                            
                            MultiFrameBuffer buffer = mMultiframeBuffers.get(arbID);
                            if (buffer == null) {
                                throw new CanClientException("Buffer for " + arbID + " not found");
                            }
                            
                            buffer.append(messageData, index);
                            
                            if (buffer.isComplete()) {
                                fireCanMessageReceivedEvent(
                                    new CanMessage(arbID, buffer.getData())
                                );
                                mMultiframeBuffers.remove(arbID);
                            }
                            
                            break;
                        }
                            
                        case PCITYPE_FLOW_CONTROL_FRAME:
                            // TODO: 
                            break;
                            
                        default:
                            throw new CanClientException("Unexpected PCITYPE " + pciType);
                    }
                            
                } else {
                    fireCanMessageReceivedEvent(
                        new CanMessage(arbID, frame.getData())
                    );
                }
            } catch (CanClientException ex) {
                fireErrorEvent(ex);
            } catch (CanMessageException e1) {
                fireErrorEvent(new CanClientException("CanMessage error: " + e1.getMessage()));
            }
        }
        @Override
        public void handleErrorEvent(CanAdapterException e) {
            fireErrorEvent(new CanClientException("Adapter error: " + e.getMessage()));
        }
    };
            
    private synchronized void fireConnectedStateChangeEvent(boolean isConnected)
    {
        Iterator<OnClientConnectedStateChangeListener> i = mClientConnectedStateChangeListeners.iterator();
        while(i.hasNext())  {
            ((OnClientConnectedStateChangeListener) i.next()).handleClientConnectedStateChanged(isConnected);
        }
    }
    
    public CanClient(CanBusSpecs specs)
    {
        this.specs = specs;
    }
    
    public CanClient connect() throws CanClientException
    {
        if (this.isConnected()) {
            return this;
        }
        
        if (adapter == null) {
            throw new CanClientException("Adapter not specified");
        }
        adapter.addEventListener(mCanFrameEventClassListener);
        try {
            adapter.setBusSpecs(specs);
            adapter.connect();
            
            fireConnectedStateChangeEvent(true);
        } catch (CanAdapterException e) {
            throw new CanClientException("Adapter error: " + e.getMessage());
        }
        
        return this;
    }
    
    public CanClient disconnect()
    {
        this.stopTimers();
        
        adapter.disconnect();
        
        adapter.removeEventListener(mCanFrameEventClassListener);
        
        fireConnectedStateChangeEvent(false);
        
        return this;
    }
    
    public CanClient stopTimers()
    {
        for (Timer t : this.timers) {
            t.cancel();
        }
        this.timers.clear();
        
        return this;
    }
    
    public boolean isConnected()
    {
        return adapter != null && adapter.isConnected();
    }
    
    public CanClient send(CanFrame message) throws CanClientException
    {
        if (!this.isConnected()) {
            throw new CanClientException("CanClient is not connected");
        }
        
        try {
            adapter.send(message);
        } catch (CanAdapterException e) {
            throw new CanClientException("Adapter error: " + e.getMessage());
        }
        
        return this;
    }
    
    public void setAdapter(CanAdapter adapter) {
        this.adapter = adapter;
    }
    
    public synchronized void addEventListener(OnCanFrameTransferListener listener) {
        mCanFrameTransferListeners.add(listener);
    }
    
    public synchronized void removeEventListener(OnCanFrameTransferListener listener){
        mCanFrameTransferListeners.remove(listener);
    }
    
    private synchronized void fireCanFrameSentEvent(CanFrame frame)
    {
        Iterator<OnCanFrameTransferListener> i = mCanFrameTransferListeners.iterator();
        while(i.hasNext())  {
            ((OnCanFrameTransferListener) i.next()).handleCanFrameSentEvent(frame);
        }
    }
    
    private synchronized void fireCanFrameReceivedEvent(CanFrame frame)
    {
        Iterator<OnCanFrameTransferListener> i = mCanFrameTransferListeners.iterator();
        while(i.hasNext())  {
            ((OnCanFrameTransferListener) i.next()).handleCanFrameReceivedEvent(frame);
        }
    }
    
    public synchronized void addEventListener(OnCanMessageTransferListener listener) {
        mCanMessageTransferListeners.add(listener);
    }
    
    public synchronized void removeEventListener(OnCanMessageTransferListener listener){
        mCanMessageTransferListeners.remove(listener);
    }
    
    private synchronized void fireCanMessageSentEvent(CanMessage message)
    {
        Iterator<OnCanMessageTransferListener> i = mCanMessageTransferListeners.iterator();
        while(i.hasNext())  {
            ((OnCanMessageTransferListener) i.next()).handleCanMessageSentEvent(message);
        }
    }
    
    private synchronized void fireCanMessageReceivedEvent(CanMessage message)
    {
        Iterator<OnCanMessageTransferListener> i = mCanMessageTransferListeners.iterator();
        while(i.hasNext())  {
            ((OnCanMessageTransferListener) i.next()).handleCanMessageReceivedEvent(message);
        }
    }
    
    public synchronized void addEventListener(OnCanClientErrorListener listener) {
        mErrorListeners.add(listener);
    }
    
    public synchronized void removeEventListener(OnCanClientErrorListener listener){
        mErrorListeners.remove(listener);
    }
    
    private synchronized void fireErrorEvent(CanClientException e)
    {
        Iterator<OnCanClientErrorListener> i = mErrorListeners.iterator();
        while(i.hasNext())  {
            ((OnCanClientErrorListener) i.next()).handleErrorEvent(e);
        }
    }
    
    public void addTimerTaskFrame(CanFrame frame, long delay, long period)
    {
        addTimerTaskFrame(frame, delay, period, false);
    }
    
    public void addTimerTaskFrame(CanFrame frame, long delay, long period, boolean receive)
    {
        Timer timer = new Timer();
        timer.schedule(new FrameTimerTask(this, frame, receive), delay, period);
        
        this.timers.add(timer);
    }
    
    public class FrameTimerTask extends TimerTask {
        private CanClient client;
        private CanFrame frame;
        private boolean receive;
        
        public FrameTimerTask(CanClient client, CanFrame frame)
        {
            this(client, frame, false);
        }
        
        public FrameTimerTask(CanClient client, CanFrame frame, boolean receive)
        {
            this.client = client;
            this.frame = frame;
            this.receive = receive;
        }
        
        public void run() {
            try {
                this.client.send(this.frame);
                if (receive) {
                    this.client.receive(this.frame);
                }
            } catch (CanClientException e) {
                fireErrorEvent(e);
            }
        }
    }
    
    public void sendDelayedFrame(final CanFrame frame, final int delay) {
        sendDelayedFrame(frame, delay, false);
    }

    public void sendDelayedFrame(final CanFrame frame, final int delay, final boolean receive) {
        final CanClient client = this;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    client.send(frame);
                    if (receive) {
                        client.receive(frame);
                    }
                } catch (CanClientException e) {
                    fireErrorEvent(e);
                }
            }
        }, delay);
    }
    
    public void receive(final CanFrame frame)
    {
        adapter.receive(frame);
    }

    private class MultiFrameBuffer {
        int currentLength;
        int lastCounter;
        byte[] buffer;
        
        public MultiFrameBuffer(int expectedLength)
        {
            this.currentLength = 0;
            this.buffer = new byte[expectedLength];
            this.lastCounter = -1; // initial value to match first 0
        }
        
        public void append(byte[] data, int cycleCounter) throws CanClientException
        {
            if (currentLength + data.length > buffer.length) {
                throw new CanClientException("Buffer overflow detected");
            }
            
            if (cycleCounter != (lastCounter + 1) % 16) {
                throw new CanClientException("Cycle counter breaks from " + lastCounter + " to " + cycleCounter);
            }
            
            System.arraycopy(data, 0, buffer, currentLength, data.length);
            
            currentLength += data.length;
            
            lastCounter = cycleCounter;
        }
        
        public boolean isComplete()
        {
            return buffer.length == currentLength;
        }
        
        public byte[] getData()
        {
            return buffer;
        }
    }
}
