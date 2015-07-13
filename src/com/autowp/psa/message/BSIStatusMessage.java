package com.autowp.psa.message;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;

public class BSIStatusMessage extends AbstractMessage {
    
    private static final int DATA_LENGTH = 8;
    private static final int ID = CanComfort.ID_BSI_STATUS;
    
    private static final byte DASHBOARD_LIGHTNING_ENABLED_BITMASK = 0x20;
    private static final byte DASHBOARD_LIGHTNING_BRIGHTNESS_BITMASK = 0x0F;
    
    private boolean mDashboardLightningEnabled;

    private byte mDashboardLightningBrightness;
    
    

    public BSIStatusMessage()
    {
        
    }

    public BSIStatusMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException("BSIStatusMessage message must have id " + ID);
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("BSIStatusMessage message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 0x0E, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, (byte) 0xA0 }, 
            new byte[] { 0x0E, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, (byte) 0xA0 }
        );
        
       
        mDashboardLightningEnabled = (data[3] & DASHBOARD_LIGHTNING_ENABLED_BITMASK) != 0x00;
        mDashboardLightningBrightness = (byte) (data[3] & DASHBOARD_LIGHTNING_BRIGHTNESS_BITMASK);
    }

    public boolean isDashboardLightningEnabled() {
        return mDashboardLightningEnabled;
    }

    public void setDashboardLightningEnabled(boolean dashboardLightningEnabled) {
        mDashboardLightningEnabled = dashboardLightningEnabled;
    }

    public byte getDashboardLightningBrightness() {
        return mDashboardLightningBrightness;
    }

    public void setDashboardLightningBrightness(byte dashboardLightningBrightness) {
        mDashboardLightningBrightness = (byte) (dashboardLightningBrightness & DASHBOARD_LIGHTNING_BRIGHTNESS_BITMASK);
    }
    
    public CanFrame assembleFrame() throws CanFrameException
    {
        byte data[] = {0x0E, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, (byte) 0xA0};
        
        data[3] = (byte) ((mDashboardLightningBrightness & DASHBOARD_LIGHTNING_BRIGHTNESS_BITMASK)
                | (mDashboardLightningEnabled ? DASHBOARD_LIGHTNING_ENABLED_BITMASK : 0x00));
        
        return new CanFrame(ID, data);
    }
}
