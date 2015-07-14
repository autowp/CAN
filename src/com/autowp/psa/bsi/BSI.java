package com.autowp.psa.bsi;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.autowp.can.CanClient;
import com.autowp.can.CanClientException;
import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.psa.CanComfort;
import com.autowp.psa.message.BSIInfoMessage;
import com.autowp.psa.message.BSIInfoWindowMessage;
import com.autowp.psa.message.BSIStatusMessage;
import com.autowp.psa.message.BSIVINMessage;
import com.autowp.psa.message.MessageException;

public class BSI {
    private CanClient mCanClient;
    private boolean mReceive = true;
    
    private BSIInfoMessage mInfoMessage = new BSIInfoMessage();
    private Timer mInfoTimer;
    
    private BSIInfoWindowMessage mInfowWindowMessage = new BSIInfoWindowMessage();
    private Timer mInfoWindowTimer;
    
    private BSIStatusMessage mStatusMessage = new BSIStatusMessage();
    private Timer mStatusTimer;
    
    private BSIVINMessage mVINMessage = new BSIVINMessage();
    private Timer mVINTimer;
    
    private class InfoTimerTask extends TimerTask {
        public void run() {
            try {
                CanFrame frame = mInfoMessage.assembleFrame();
                mCanClient.send(frame);
                if (mReceive) {
                    mCanClient.receive(frame);
                }
            } catch (CanFrameException | CanClientException e) {
                //fireErrorEvent(e);
            }
        }
    }
    
    
    private class VINTimerTask extends TimerTask {
        public void run() {
            try {
                CanFrame frame = mVINMessage.assembleFrame();
                mCanClient.send(frame);
                if (mReceive) {
                    mCanClient.receive(frame);
                }
            } catch (CanFrameException | CanClientException e) {
                //fireErrorEvent(e);
            }
        }
    }
    
    private class StatusTimerTask extends TimerTask {
        public void run() {
            try {
                CanFrame frame = mStatusMessage.assembleFrame();
                mCanClient.send(frame);
                if (mReceive) {
                    mCanClient.receive(frame);
                }
            } catch (CanFrameException | CanClientException e) {
                //fireErrorEvent(e);
            }
        }
    }
    
    private class InfowWindowTimerTask extends TimerTask {
        public void run() {
            
            try {
                CanFrame frame = mInfowWindowMessage.assembleFrame();
                mCanClient.send(frame);
                if (mReceive) {
                    mCanClient.receive(frame);
                }
                if (mInfowWindowMessage.getAction() == BSIInfoWindowMessage.ACTION_HIDE) {
                    mInfowWindowMessage.setAction(BSIInfoWindowMessage.ACTION_CLEAR);
                }
            } catch (CanClientException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (CanFrameException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }

    public BSI(CanClient canClient) {
        mCanClient = canClient;
        
        
    }

    public String getVIN() {
        return mVINMessage.getVIN();
    }

    public void setVIN(String VIN) throws MessageException {
        mVINMessage.setVIN(VIN);
    }
    
    public void startStatus() throws CanFrameException
    {
        stopStatus();
        
        StatusTimerTask task = new StatusTimerTask();
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.BSI_STATUS_PERIOD);
        
        mStatusTimer = timer;
    }
    
    public void stopStatus()
    {
        if (mStatusTimer != null) {
            mStatusTimer.cancel();
            mStatusTimer = null;
        }
    }
    
    public void startInfo() throws CanFrameException
    {
        stopInfo();
        
        final Random random = new Random();
        mInfoMessage.setGear((byte) random.nextInt(7));
        mInfoMessage.setIsReverse(random.nextBoolean());
        mInfoMessage.setTemperature(random.nextFloat() * 125 - 40);
        mInfoMessage.setOdometer(random.nextInt(0x00FFFFFF));
        
        InfoTimerTask task = new InfoTimerTask();
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.BSI_INFO_PERIOD);
        
        mInfoTimer = timer;
    }
    
    public void stopInfo()
    {
        if (mInfoTimer != null) {
            mInfoTimer.cancel();
            mInfoTimer = null;
        }
    }

    public boolean isStatusStarted() {
        return mStatusTimer != null;
    }
    
    public boolean isInfoStarted() {
        return mInfoTimer != null;
    }

    public boolean isReceive() {
        return mReceive;
    }

    public void setReceive(boolean receive) {
        mReceive = receive;
    }
    
    public void startInfoWindow() throws CanFrameException
    {
        stopInfoWindow();
        
        InfowWindowTimerTask task = new InfowWindowTimerTask();
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.BSI_INFO_WINDOW_PERIOD);
        
        mInfoWindowTimer = timer;
    }
    
    public void stopInfoWindow()
    {
        if (mInfoWindowTimer != null) {
            mInfoWindowTimer.cancel();
            mInfoWindowTimer = null;
        }
    }
    
    public boolean isInfoWindowStarted() {
        return mInfoWindowTimer != null;
    }
    
    public void setInfoWindowAction(byte action) {
        mInfowWindowMessage.setAction(action);
    }

    public void setInfoWindowCode(byte code) {
        mInfowWindowMessage.setCode(code);
    }

    public void setDahsboardLightingEnabled(boolean enabled) {
        mStatusMessage.setDashboardLightningEnabled(enabled);
    }

    public void setDashboardLightingBrightness(byte value) {
        mStatusMessage.setDashboardLightningBrightness(value);
    }

    public byte getDashboardLightingBrightness() {
        return mStatusMessage.getDashboardLightningBrightness();
    }

    public boolean isDashboardLightingEnabled() {
        return mStatusMessage.isDashboardLightningEnabled();
    }
    
    public void startVIN()
    {
        stopVIN();
        
        VINTimerTask task = new VINTimerTask();
        
        Timer timer = new Timer();
        timer.schedule(task, 0, CanComfort.BSI_VIN_PERIOD);
        
        mVINTimer = timer;
    }
    
    public void stopVIN()
    {
        if (mVINTimer != null) {
            mVINTimer.cancel();
            mVINTimer = null;
        }
    }
    
    public boolean isVINStarted() {
        return mVINTimer != null;
    }
    
    public void setTemperature(double temperature) {
        mInfoMessage.setTemperature(temperature);
    }
    
    public double getTemperature() {
        return mInfoMessage.getTemperature();
    }
}
