package com.autowp.peugeot.parktronic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.autowp.can.CanClient;
import com.autowp.can.CanMessage;
import com.autowp.peugeot.CanComfort;
import com.autowp.peugeot.message.MessageException;
import com.autowp.peugeot.message.ParktronicMessage;

public class Parktronic {
    
    private int mFrontLeft;
    
    private int mFrontRight;
    
    private int mFrontCenter;
    
    private int mRearLeft;
    
    private int mRearRight;
    
    private int mRearCenter;
    
    private int mSoundPeriod;
    
    private boolean mShow = false;
    
    private boolean mSoundEnabled = false;
    
    public interface OnParktronicStateChangedListener {
        public void handleStateChangedEvent(Parktronic parktronic);
    }
    
    private List<OnParktronicStateChangedListener> mStateChangedListeners = 
            new ArrayList<OnParktronicStateChangedListener>();
    
    private CanClient.OnCanMessageTransferListener canMessageListener = new CanClient.OnCanMessageTransferListener() {
        
        @Override
        public void handleCanMessageSentEvent(CanMessage message) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void handleCanMessageReceivedEvent(CanMessage message) {
            try {
                processMessage(message);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            
        }
    };
    
    @SuppressWarnings("serial")
    private static final HashMap<Integer, Integer> sFrontCenterMap = new HashMap<Integer, Integer>() {{
        put(0, 4);
        put(1, 3);
        put(2, 3);
        put(3, 2);
        put(4, 2);
        put(5, 1);
        put(6, 1);
        put(7, 0);
    }};
    
    @SuppressWarnings("serial")
    private static final HashMap<Integer, Integer> sFrontRightMap = new HashMap<Integer, Integer>() {{
        put(0, 3);
        put(1, 2);
        put(2, 2);
        put(3, 1);
        put(4, 1);
        put(5, 0);
        put(6, 0);
        put(7, 0);
    }};
    
    @SuppressWarnings("serial")
    private static final HashMap<Integer, Integer> sRearSideMap = new HashMap<Integer, Integer>() {{
        put(0, 3);
        put(1, 2);
        put(2, 1);
        put(3, 0);
        put(4, 0);
        put(5, 0);
        put(6, 0);
        put(7, 0);
    }};
    
    @SuppressWarnings("serial")
    private static final HashMap<Integer, Integer> sRearCenterMap = new HashMap<Integer, Integer>() {{
        put(0, 4);
        put(1, 4);
        put(2, 4);
        put(3, 4);
        put(4, 3);
        put(5, 3);
        put(6, 3);
        put(7, 3);
        put(8, 3);
        put(9, 3);
        put(10, 3);
        put(11, 3);
        put(12, 2);
        put(13, 2);
        put(14, 2);
        put(15, 2);
        put(16, 2);
        put(17, 2);
        put(18, 2);
        put(19, 2);
        put(20, 1);
        put(21, 1);
        put(22, 1);
        put(23, 1);
        put(24, 1);
        put(25, 1);
        put(26, 1);
        put(27, 1);
        put(28, 0);
        put(29, 0);
        put(30, 0);
        put(31, 0);
    }};
    
    @SuppressWarnings("serial")
    private static final HashMap<Integer, Integer> sFrontLeftMap = new HashMap<Integer, Integer>() {{
        put(0, 3);
        put(1, 3);
        put(2, 3);
        put(3, 3);
        put(4, 2);
        put(5, 2);
        put(6, 2);
        put(7, 2);
        put(8, 2);
        put(9, 2);
        put(10, 2);
        put(11, 2);
        put(12, 1);
        put(13, 1);
        put(14, 1);
        put(15, 1);
        put(16, 1);
        put(17, 1);
        put(18, 1);
        put(19, 1);
        put(20, 0);
        put(21, 0);
        put(22, 0);
        put(23, 0);
        put(24, 0);
        put(25, 0);
        put(26, 0);
        put(27, 0);
        put(28, 0);
        put(29, 0);
        put(30, 0);
        put(31, 0);
    }};
    
    
    
    public Parktronic(CanClient client)
    {
        setCanClient(client);
    }
    
    public void setCanClient(CanClient client)
    {
        client.addEventListener(canMessageListener);
    }
    
    public void processMessage(CanMessage message) throws ParktronicException, MessageException
    {
        switch (message.getId()) {
            case CanComfort.ID_PARKTRONIC: {
                
                ParktronicMessage parktronicMessage = new ParktronicMessage(message);
                
                mFrontCenter = sFrontCenterMap.get(parktronicMessage.getFrontCenter());
                mFrontLeft = sFrontLeftMap.get(parktronicMessage.getFrontLeft());
                mFrontRight = sFrontRightMap.get(parktronicMessage.getFrontRight());
                mRearCenter = sRearCenterMap.get(parktronicMessage.getRearCenter());
                mRearLeft = sRearSideMap.get(parktronicMessage.getRearLeft());
                mRearRight = sRearSideMap.get(parktronicMessage.getRearRight());
                mSoundPeriod = parktronicMessage.getSoundPeriod();
                mSoundEnabled = parktronicMessage.getSoundEnabled();
                mShow = parktronicMessage.getShow();
                
                fireStateChangedEvent();
                
                break;
            }
                
            default:
                // just skip
        }
    }
    
    public int getFrontCenter()
    {
        return mFrontCenter;
    }
    
    public int getFrontLeft()
    {
        return mFrontLeft;
    }
    
    public int getFrontRight()
    {
        return mFrontRight;
    }
    
    public int getRearCenter()
    {
        return mRearCenter;
    }
    
    public int getRearLeft()
    {
        return mRearLeft;
    }
    
    public int getRearRight()
    {
        return mRearRight;
    }
    
    public int getSoundPeriod()
    {
        return mSoundPeriod;
    }
    
    public boolean getSoundEnabled()
    {
        return mSoundEnabled;
    }
    
    public boolean getShow()
    {
        return mShow;
    }
    
    public synchronized void addEventListener(OnParktronicStateChangedListener listener) {
        mStateChangedListeners.add(listener);
    }
    
    public synchronized void removeEventListener(OnParktronicStateChangedListener listener){
        mStateChangedListeners.remove(listener);
    }
    
    protected synchronized void fireStateChangedEvent()
    {
        Iterator<OnParktronicStateChangedListener> i = mStateChangedListeners.iterator();
        while(i.hasNext())  {
            ((OnParktronicStateChangedListener) i.next()).handleStateChangedEvent(this);
        }
    }
}
