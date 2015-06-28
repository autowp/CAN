package com.autowp.peugeot.message;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.peugeot.CanComfort;

public class ColumnKeypadMessage extends AbstractMessage {
    private boolean mForward;
    private boolean mBackward;
    private byte mUnknownValue;
    private boolean mVolumeUp;
    private boolean mVolumeDown;
    private boolean mSource;
    private byte mScroll;
    
    private static final byte FORWARD_BITMASK = (byte) 0x80;
    private static final byte BACKWARD_BITMASK = 0x40;
    private static final byte VOLUME_UP_BITMASK = 0x08;
    private static final byte VOLUME_DOWN_BITMASK = 0x04;
    private static final byte SOURCE_BITMASK = 0x02;
    
    public ColumnKeypadMessage()
    {
        
    }

    public ColumnKeypadMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != 3) {
            throw new MessageException("ColumnKeypad message must be 3 bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 0x01, 0x00, (byte) 0xFF}, 
            new byte[] { 0x00, 0x00,        0x00}
        );
        
        mForward  = (data[0] & FORWARD_BITMASK) != 0x00;
        mBackward = (data[0] & BACKWARD_BITMASK) != 0x00;
        mUnknownValue = (byte) (data[0] & 0x20);
        
        mVolumeUp   = (data[0] & VOLUME_UP_BITMASK) != 0x00;
        mVolumeDown = (data[0] & VOLUME_DOWN_BITMASK) != 0x00;
        mSource     = (data[0] & SOURCE_BITMASK) != 0x00;
        
        mScroll = data[1];
    }

    public boolean isForward() {
        return mForward;
    }
    
    public ColumnKeypadMessage setForward(boolean value)
    {
        mForward = value;
        return this;
    }

    public boolean isBackward() {
        return mBackward;
    }
    
    public ColumnKeypadMessage setBackward(boolean value)
    {
        mBackward = value;
        return this;
    }

    public byte getUnknownValue() {
        return mUnknownValue;
    }

    public boolean isVolumeUp() {
        return mVolumeUp;
    }
    
    public ColumnKeypadMessage setVolumeUp(boolean value)
    {
        mVolumeUp = value;
        return this;
    }

    public boolean isVolumeDown() {
        return mVolumeDown;
    }
    
    public ColumnKeypadMessage setVolumeDown(boolean value)
    {
        mVolumeDown = value;
        return this;
    }
    
    public boolean isSource() {
        return mSource;
    }
    
    public ColumnKeypadMessage setSource(boolean value)
    {
        mSource = value;
        return this;
    }
    
    public byte getScroll() {
        return mScroll;
    }
    
    public ColumnKeypadMessage setScroll(byte value)
    {
        mScroll = value;
        return this;
    }
    
    public CanFrame assembleFrame() throws CanFrameException
    {
        byte data[] = {0x00, 0x00, 0x00};
        
        if (mForward) {
            data[0] = (byte) (data[0] | FORWARD_BITMASK);
        }
        
        if (mBackward) {
            data[0] = (byte) (data[0] | BACKWARD_BITMASK);
        }
        
        if (mVolumeUp) {
            data[0] = (byte) (data[0] | VOLUME_UP_BITMASK);
        }
        
        if (mVolumeDown) {
            data[0] = (byte) (data[0] | VOLUME_DOWN_BITMASK);
        }
        
        if (mSource) {
            data[0] = (byte) (data[0] | SOURCE_BITMASK);
        }
        
        data[1] = mScroll;
        
        return new CanFrame(CanComfort.ID_COLUMN_KEYPAD, data);
    }
}
