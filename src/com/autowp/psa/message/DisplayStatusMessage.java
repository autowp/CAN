package com.autowp.psa.message;

import com.autowp.can.CanMessage;

public class DisplayStatusMessage extends AbstractMessage {
    private static final int DATA_LENGTH = 8;
    
    private static final byte OFF_BITMASK = (byte) 0x20;

    private boolean mOff;

    private byte mUnknown1;
    private byte mUnknown2;
    private byte mUnknown3;
    private byte mUnknown4;

    public DisplayStatusMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("DisplayStatus message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { (byte) 0xFF, (byte) 0xDF, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF },
            new byte[] {        0x09,        0x06, 0x00, 0x00, 0x00, 0x00,        0x00,        0x00 }
        );
        
        mOff = (data[1] & OFF_BITMASK) != 0x00;
        mUnknown1 = data[2];
        mUnknown2 = data[3];
        mUnknown3 = data[4];
        mUnknown4 = data[5];
    }

    public boolean isOff() {
        return mOff;
    }
    
    public byte getUnknown1() {
        return mUnknown1;
    }
    
    public byte getUnknown2() {
        return mUnknown2;
    }
    
    public byte getUnknown3() {
        return mUnknown3;
    }
    
    public byte getUnknown4() {
        return mUnknown4;
    }
}
