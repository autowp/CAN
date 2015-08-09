package com.autowp.psa.message;

import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;

public class RadioStatusMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_RADIO_STATUS;
    private static final int DATA_LENGTH = 4;
    
    private static final byte ENABLED_BITMASK = (byte) 0x80;
    private static final byte UNKNOWN0_BITMASK = 0x7F;
    
    private static final byte CD_CHANGER_AVAILABLE_BITMASK = 0x10;
    private static final byte CD_DISK_AVAILABLE_BITMASK = 0x60;
    private static final byte UNKNOWN1_BITMASK = (byte) 0xEF;
    
    private static final byte SOURCE_BITMASK = 0x70;
    private boolean mEnabled;
    private byte mSource;
    private byte mUnknown0;
    private byte mUnknown1;
    private boolean mCDChangerAvailable;
    private boolean mCDDiskAvailable;
    
    public RadioStatusMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("RadioSource message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("RadioSource message must be " + DATA_LENGTH + " bytes long");
        }

        this.assertConstBits(
            data,
            new byte[] { 
                ~(ENABLED_BITMASK | UNKNOWN0_BITMASK),
                ~(CD_CHANGER_AVAILABLE_BITMASK | CD_DISK_AVAILABLE_BITMASK | UNKNOWN1_BITMASK),
                ~(SOURCE_BITMASK),
                (byte) 0xFF
            }, 
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x00
            }
        );
        
        mEnabled = (data[0] & ENABLED_BITMASK) != 0x00;
        mUnknown0 = (byte)(data[0] & UNKNOWN0_BITMASK);
        
        mCDChangerAvailable = (data[1] & CD_CHANGER_AVAILABLE_BITMASK) != 0x00;
        byte cdAvailibility = (byte) (data[1] & CD_DISK_AVAILABLE_BITMASK);
        if (cdAvailibility == 0x20) {
            mCDDiskAvailable = false;
        } else if (cdAvailibility == 0x40) {
            mCDDiskAvailable = true;
        } else if (cdAvailibility == 0x00) {
            // initialization
        } else {
            throw new MessageException(
                String.format("Unexpected CD disk avalibiity value %02X", cdAvailibility)
            );
        }
         
        mUnknown1 = (byte)(data[1] & UNKNOWN1_BITMASK);
        
        mSource = (byte) ((data[2] & SOURCE_BITMASK) >> 4);
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public byte getSource() {
        return mSource;
    }

    public void setSource(byte source) {
        mSource = source;
    }
    
    public byte getUnknown0() {
        return mUnknown0;
    }
    
    public byte getUnknown1() {
        return mUnknown1;
    }

    public boolean isCDChangerAvailable() {
        return mCDChangerAvailable;
    }

    public void setCDChangerAvailable(boolean cdChangerAvailable) {
        mCDChangerAvailable = cdChangerAvailable;
    }

    public boolean isCDDiskAvailable() {
        return mCDDiskAvailable;
    }

    public void setCDDiskAvailable(boolean mCDDiskAvailable) {
        this.mCDDiskAvailable = mCDDiskAvailable;
    }
}
