package com.autowp.psa.cdchanger;

import java.util.Timer;
import java.util.TimerTask;

import com.autowp.can.CanClient;
import com.autowp.can.CanClientException;
import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.psa.CanComfort;
import com.autowp.psa.message.AbstractMessage;
import com.autowp.psa.message.cdchanger.*;

public class CDChanger {
    private CanClient mCanClient;
    private boolean mLoopback = true;
    
    private CDChangerWelcomeMessage mWelcomeMessage = new CDChangerWelcomeMessage();
    
    private CDChangerPingMessage mPingMessage = new CDChangerPingMessage();
    private Timer mPingTimer;
    
    private CDChangerTracksCountMessage mTracksCountMessage = new CDChangerTracksCountMessage();
    private Timer mTracksCountTimer;
    
    private CDChangerStatusMessage mStatusMessage = new CDChangerStatusMessage();
    private Timer mStatusTimer;
    
    private CDChangerDiskMessage mDiskMessage = new CDChangerDiskMessage();
    private Timer mDiskTimer;
    
    private CDChangerDisk2Message mDisk2Message = new CDChangerDisk2Message();
    private Timer mDisk2Timer;
    
    private CDChangerCurrentTrackMessage mCurrentTrackMessage = new CDChangerCurrentTrackMessage();
    private Timer mCurrentTrackTimer;
    
    private class MessageTimerTask extends TimerTask {
        private AbstractMessage mMessage;
        
        public MessageTimerTask(AbstractMessage message) {
            super();
            
            mMessage = message;
        }
        
        public void run() {
            try {
                CanFrame frame = mMessage.assembleFrame();
                mCanClient.send(frame);
                if (mLoopback) {
                    mCanClient.receive(frame);
                }
            } catch (CanFrameException | CanClientException e) {
                //fireErrorEvent(e);
            }
        }
    }
    
    public CDChanger(CanClient canClient) {
        mCanClient = canClient;
    }
    
    public void start() throws CanClientException, CanFrameException {
        mCanClient.send(mWelcomeMessage.assemble());
        
        startStatus();
        startPing();
        startTracksCount();
        startDisk();
        startDisk2();
        startCurrentTrack();
    }
    
    public void stop() {
        stopStatus();
        stopPing();
        stopTracksCount();
        stopDisk();
        stopDisk2();
        stopCurrentTrack();
    }
    
    public void startStatus() throws CanFrameException
    {
        stopStatus();
        
        MessageTimerTask task = new MessageTimerTask(mStatusMessage);
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.CD_CHANGER_STATUS_PERIOD);
        
        mStatusTimer = timer;
    }
    
    public void stopStatus()
    {
        if (mStatusTimer != null) {
            mStatusTimer.cancel();
            mStatusTimer = null;
        }
    }
    
    public void startPing() throws CanFrameException
    {
        stopPing();
        
        MessageTimerTask task = new MessageTimerTask(mPingMessage);
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.CD_CHANGER_PING_PERIOD);
        
        mPingTimer = timer;
    }
    
    public void stopPing()
    {
        if (mPingTimer != null) {
            mPingTimer.cancel();
            mPingTimer = null;
        }
    }
    
    public void startTracksCount() throws CanFrameException
    {
        stopTracksCount();
        
        MessageTimerTask task = new MessageTimerTask(mTracksCountMessage);
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.CD_CHANGER_TRACKS_COUNT_PERIOD);
        
        mTracksCountTimer = timer;
    }
    
    public void stopTracksCount()
    {
        if (mTracksCountTimer != null) {
            mTracksCountTimer.cancel();
            mTracksCountTimer = null;
        }
    }
    
    public void startDisk() throws CanFrameException
    {
        stopDisk();
        
        MessageTimerTask task = new MessageTimerTask(mDiskMessage);
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.CD_CHANGER_DISK_PERIOD);
        
        mDiskTimer = timer;
    }
    
    public void stopDisk()
    {
        if (mDiskTimer != null) {
            mDiskTimer.cancel();
            mDiskTimer = null;
        }
    }
    
    public void startDisk2() throws CanFrameException
    {
        stopDisk2();
        
        mDisk2Message.setLoading(false);
        mDisk2Message.setUnknown0(true);
        
        MessageTimerTask task = new MessageTimerTask(mDisk2Message);
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.CD_CHANGER_DISK2_PERIOD);
        
        mDisk2Timer = timer;
    }
    
    public void stopDisk2()
    {
        if (mDisk2Timer != null) {
            mDisk2Timer.cancel();
            mDisk2Timer = null;
        }
    }
    
    public void startCurrentTrack() throws CanFrameException
    {
        stopCurrentTrack();
        
        MessageTimerTask task = new MessageTimerTask(mCurrentTrackMessage);
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.CD_CHANGER_CURRENT_TRACK_PERIOD);
        
        mCurrentTrackTimer = timer;
    }
    
    public void stopCurrentTrack()
    {
        if (mCurrentTrackTimer != null) {
            mCurrentTrackTimer.cancel();
            mCurrentTrackTimer = null;
        }
    }
    
    public boolean isStatusStarted() {
        return mStatusTimer != null;
    }
    
    public boolean isCurrentTrackStarted() {
        return mCurrentTrackTimer != null;
    }
    
    public boolean isDisk2Started() {
        return mDisk2Timer != null;
    }
    
    public boolean isDiskStarted() {
        return mDiskTimer != null;
    }
    
    public boolean isPingStarted() {
        return mPingTimer != null;
    }
    
    public boolean isTracksCountStarted() {
        return mTracksCountTimer != null;
    }
    
    public void setLoopback(boolean value) {
        mLoopback = value;
    }
    
    public boolean isLoopback() {
        return mLoopback;
    }
    
    public void setDisk(byte disk) {
        mDiskMessage.setDisk(disk);
        mDisk2Message.setDisk(disk);
    }
    
    public byte getDisk() {
        return mDiskMessage.getDisk();
    }
    
    public void setCurrentTrack(byte track) {
        mCurrentTrackMessage.setTrackNumber(track);
    }
    
    public byte getCurrentTrack() {
        return mCurrentTrackMessage.getTrackNumber();
    }
    
    public byte getTotalTracks() {
        return mTracksCountMessage.getCount();
    }
    
    public void setTotalTracks(byte value) {
        mTracksCountMessage.setCount(value);
    }
}
