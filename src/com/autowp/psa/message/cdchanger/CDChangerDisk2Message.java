package com.autowp.psa.message.cdchanger;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;
import com.autowp.psa.message.AbstractMessage;
import com.autowp.psa.message.MessageException;

public class CDChangerDisk2Message extends AbstractMessage {
    private static final int ID = CanComfort.ID_CD_CHANGER_DISK2;
    private static final int DATA_LENGTH = 7;
    
    private static final byte UNKNOWN0_BITMASK = (byte) 0x80;
    
    private static final byte LOADING_BITMASK = (byte) 0x80;
    private static final byte ICON_BITMASK = 0x07;
    
    private static final byte DISK_BITMASK = 0x0F;
    
    public static final byte ICON_NONE = 0;
    public static final byte ICON_STOP = 1;
    public static final byte ICON_PAUSE = 2;
    public static final byte ICON_PLAY = 3;
    public static final byte ICON_FAST_FORWARD = 4;
    public static final byte ICON_FAST_BACKWARD = 5;
    public static final byte ICON_NONE2 = 6;
    public static final byte ICON_PLAY2 = 7;
    
    private byte mDisk = 1;
    private boolean mLoading;
    private boolean mUnknown0;
    private byte mIcon;
    private byte mUnknown5;
    
    public CDChangerDisk2Message() {
        
    }
    
    public CDChangerDisk2Message(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("CDChangerDisk2 message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("CDChangerDisk2 message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 
                ~UNKNOWN0_BITMASK,
                ~(LOADING_BITMASK | ICON_BITMASK),
                (byte) 0xFF,
                ~DISK_BITMASK,
                (byte) 0xFF,
                (byte) 0x00,
                (byte) 0xFF
            },
            new byte[] { 
                0x20,
                0x00,
                0x06,
                0x00,
                0x00,
                0x00,
                0x00
            }
        );
        
        mUnknown0 = (data[0] & UNKNOWN0_BITMASK) != 0x00;
        
        mLoading = (data[1] & LOADING_BITMASK) != 0x00;
        mIcon = (byte) (data[1] & ICON_BITMASK);
        
        mDisk  = (byte) (data[3] & DISK_BITMASK);

    }

    public byte getDisk() {
        return mDisk;
    }

    public void setDisk(byte disk) {
        mDisk = disk;
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public boolean isUnknown0() {
        return mUnknown0;
    }

    public void setUnknown0(boolean unknown0) {
        mUnknown0 = unknown0;
    }

    public byte getIcon() {
        return mIcon;
    }

    public void setIcon(byte icon) {
        mIcon = icon;
    }
    
    public byte getUnknown5() {
        return mUnknown5;
    }

    public void setUnknown5(byte unknown5) {
        mUnknown5 = unknown5;
    }
    
    public CanFrame assembleFrame() throws CanFrameException 
    {
        byte data[] = {0x20, 0x00, 0x06, 0x00, 0x00, 0x01, 0x00};
        
        if (mUnknown0) {
            data[0] = (byte) (data[0] | UNKNOWN0_BITMASK);
        }
        
        if (mLoading) {
            data[1] = (byte) (data[1] | LOADING_BITMASK);
        }
        data[1] = (byte) (((mIcon & ICON_BITMASK)) | data[1]);
        data[3] = (byte) (((mDisk & DISK_BITMASK)) | data[3]);
        
        return new CanFrame(ID, data);
    }
}
