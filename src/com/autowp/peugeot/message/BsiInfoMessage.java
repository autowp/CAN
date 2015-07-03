package com.autowp.peugeot.message;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.peugeot.CanComfort;

public class BsiInfoMessage extends AbstractMessage {
    private static final int DATA_LENGTH = 8;
    
    private static final byte REVERSE_BITMASK = (byte) 0x80;
    private static final byte GEAR_BITMASK = 0x07;

    private float mTemperature;

    private boolean mReverse;

    private byte mGear;

    private int mOdometer;
    
    public BsiInfoMessage()
    {
        
    }

    public BsiInfoMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("BsiInfoMessage message must be " + DATA_LENGTH + " bytes long");
        }
        
        // 10001110 10000111 00001011 10110010 00011100 10001110
        
        this.assertConstBits(
            data,
            new byte[] { (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 0x00, (byte) 0xFF, 0x00, 0x78}, 
            new byte[] { (byte) 0x8E, (byte) 0x87, 0x00, 0x00, 0x00, (byte) 0x8E, 0x00, 0x00 }
        );
        
        int temperature = data[6] & 0x00FF;
        mTemperature = (float) ( (temperature - 79) / 2.0 );
        
        mReverse = (data[7] & REVERSE_BITMASK) == 0x00;
        mGear = (byte) (data[7] & GEAR_BITMASK);
        
        mOdometer = ((data[2] << 16) & 0x00FF0000) + ((data[3] << 8) & 0x0000FF00) + (data[4] & 0x000000FF);
    }

    public float getTemperature() {
        return mTemperature;
    }
    
    public void setTemperature(float value) {
        this.mTemperature = value;
    }

    public boolean isReverse() {
        return mReverse;
    }
    
    public void setIsReverse(boolean value) {
        mReverse = value;
    }

    public byte getGear() {
        return mGear;
    }

    public void setGear(byte gear) {
        this.mGear = gear;
    }

    public CanFrame assembleFrame() throws CanFrameException
    {
        byte data[] = {(byte)0x8E, (byte)0x87, 0x00, 0x00, 0x00, (byte)0x8E, 0x00, 0x00};
        
        data[2] = (byte) ((mOdometer >> 16) & 0xFF);
        data[3] = (byte) ((mOdometer >> 8) & 0xFF);
        data[4] = (byte) (mOdometer & 0xFF);
        
        data[6] = (byte) ((mTemperature + 40) * 2);
        
        if (mReverse) {
            data[7] = (byte) (data[7] | REVERSE_BITMASK);
        }
        data[7] = (byte) (data[7] | mGear);
        
        return new CanFrame(CanComfort.ID_BSI_INFO, data);
    }

    public int getOdometer() {
        return mOdometer;
    }

    public void setOdometer(int mOdometer) {
        this.mOdometer = mOdometer;
    }
}
