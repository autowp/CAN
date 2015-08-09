package com.autowp.psa.message;

import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;

public class DisplayUnknown1Message extends AbstractMessage {
    private static final int ID = CanComfort.ID_DISPLAY_UNKNOWN1;
    private static final int DATA_LENGTH = 3;
    
    private static final byte UNKNOWN1_BITMASK = (byte) 0x80;
    private static final byte UNKNOWN3_BITMASK = (byte) 0x03;
    
    private static final byte UNKNOWN4_BITMASK = (byte) 0x70;
    private static final byte UNKNOWN5_BITMASK = (byte) 0x07;
    
    private static final byte UNKNOWN2_BITMASK = (byte) 0x20;

    private boolean mUnknown1;

    private boolean mUnknown2;

    private byte mUnknown3;
    
    private byte mUnknown4;
    
    private byte mUnknown5;

    public DisplayUnknown1Message(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("DisplayUnknown1 message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("DisplayUnknown1 message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] {
                ~(UNKNOWN1_BITMASK | UNKNOWN3_BITMASK), 
                ~(UNKNOWN4_BITMASK | UNKNOWN5_BITMASK),
                ~UNKNOWN2_BITMASK
            }, 
            new byte[] { 
                0x10,
                0x00,
                0x50
            }
        );
        
        mUnknown1 = (data[0] & UNKNOWN1_BITMASK) != 0x00;
        mUnknown3 = (byte) (data[0] & UNKNOWN3_BITMASK);
        
        mUnknown4 = (byte) ((byte) (data[1] & UNKNOWN4_BITMASK) >> 4);
        mUnknown5 = (byte) (data[1] & UNKNOWN5_BITMASK);
        
        mUnknown2 = (data[2] & UNKNOWN2_BITMASK) != 0x00;
    }

    public boolean isUnknown1() {
        return mUnknown1;
    }

    public boolean isUnknown2() {
        return mUnknown2;
    }

    public byte getUnknown3() {
        return mUnknown3;
    }

    public byte getUnknown4() {
        return mUnknown4;
    }
    
    public byte getUnknown5() {
        return mUnknown5;
    }
}
