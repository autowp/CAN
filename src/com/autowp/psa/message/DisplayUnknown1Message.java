package com.autowp.psa.message;

import com.autowp.can.CanMessage;

public class DisplayUnknown1Message extends AbstractMessage {
    private static final int DATA_LENGTH = 3;
    
    private static final byte UNKNOWN1_BITMASK = (byte) 0x80;
    private static final byte UNKNOWN2_BITMASK = (byte) 0x20;

    private boolean mUnknown1;

    private boolean mUnknown2;

    public DisplayUnknown1Message(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("DisplayStatus message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 0x7F, (byte) 0xFF, (byte) 0xD0}, 
            new byte[] { 0x10,        0x00,        0x50}
        );
        
        mUnknown1 = (data[0] & UNKNOWN1_BITMASK) != 0x00;
        mUnknown2 = (data[2] & UNKNOWN2_BITMASK) != 0x00;
    }

    public boolean isUnknown1() {
        return mUnknown1;
    }

    public boolean isUnknown2() {
        return mUnknown2;
    }
}
