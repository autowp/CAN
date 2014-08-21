package com.autowp.can;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;


public abstract class CanAdapter {
    public abstract void send(CanFrame message) throws CanAdapterException;
    
    public abstract void connect() throws CanAdapterException;
    
    public abstract void disconnect();
    
    public abstract boolean isConnected();
    
    public interface CanAdapterEventListener {
        public void handleCanFrameReceivedEvent(CanFrameEvent e);
        
        public void handleCanFrameSentEvent(CanFrameEvent e);
        
        public void handleErrorEvent(CanAdapterException e);
    }
    
    @SuppressWarnings("serial")
    public class CanFrameEvent extends EventObject {
        protected CanFrame frame;

        public CanFrameEvent(Object source, CanFrame frame) {
            super(source);
            this.frame = frame;
        }
        
        public CanFrame getFrame() {
            return frame;
        }
    }
    
    private List<CanAdapterEventListener> canFrameEventListeners = new ArrayList<CanAdapterEventListener>();
    
    protected CanBusSpecs specs;
    
    public synchronized void addEventListener(CanAdapterEventListener listener) 
    {
        canFrameEventListeners.add(listener);
    }
    
    public synchronized void removeEventListener(CanAdapterEventListener listener)
    {
        canFrameEventListeners.remove(listener);
    }
    
    protected synchronized void fireFrameSentEvent(CanFrame frame)
    {
        CanFrameEvent event = new CanFrameEvent(this, frame);
        Iterator<CanAdapterEventListener> i = canFrameEventListeners.iterator();
        while(i.hasNext()) {
            ((CanAdapterEventListener) i.next()).handleCanFrameSentEvent(event);
        }
    }
    
    protected synchronized void fireFrameReceivedEvent(CanFrame frame)
    {
        CanFrameEvent event = new CanFrameEvent(this, frame);
        Iterator<CanAdapterEventListener> i = canFrameEventListeners.iterator();
        while(i.hasNext()) {
            ((CanAdapterEventListener) i.next()).handleCanFrameReceivedEvent(event);
        }
    }
    
    protected synchronized void fireErrorEvent(CanAdapterException e)
    {
        Iterator<CanAdapterEventListener> i = canFrameEventListeners.iterator();
        while(i.hasNext()) {
            ((CanAdapterEventListener) i.next()).handleErrorEvent(e);
        }
    }
    
    public void setBusSpecs(CanBusSpecs specs)
    {
        this.specs = specs;
    }

    public void receive(CanFrame frame) {
        fireFrameReceivedEvent(frame);
    }
}
