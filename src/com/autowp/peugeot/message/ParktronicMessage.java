package com.autowp.peugeot.message;

import com.autowp.can.CanMessage;

public class ParktronicMessage extends AbstractMessage {
    
    private int mFrontLeft = 0;
    
    private int mFrontRight = 0;
    
    private int mFrontCenter = 0;
    
    private int mRearLeft = 0;
    
    private int mRearRight = 0;
    
    private int mRearCenter = 0;
    
    private int mSoundPeriod = 0;
    
    private boolean mShow = false;
    
    private boolean mSoundEnabled = false;
    
    public ParktronicMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != 7) {
            throw new MessageException("Parktronic message must be 7 bytes long");
        }
        
        int soundEnabled = (int)data[1] & 0xF0;
        if (soundEnabled == 0xF0) {
            mSoundEnabled = true;
        } else if (soundEnabled == 0x00) {
            mSoundEnabled = false;
        } else {
            throw new MessageException("Unexpected sound_enabled value");
        }
        
        mSoundPeriod = (int)data[2] & 0x3F;
        
        mRearLeft = (int)(data[3] >> 5) & 0x07;
        mRearCenter = (int)data[3] & 0x1F;
        
        mRearRight = (int)(data[4] >> 5) & 0x07;
        mFrontLeft = (int)data[4] & 0x1F;
        
        mFrontCenter = (int)(data[5] >> 5) & 0x07;
        mFrontRight = (int)(data[5] >> 2) & 0x07;
    }
    
    public int getFrontLeft()
    {
        return mFrontLeft;
    }
    
    public int getFrontRight()
    {
        return mFrontRight;
    }
    
    public int getFrontCenter()
    {
        return mFrontCenter;
    }
    
    public int getRearLeft()
    {
        return mRearLeft;
    }
    
    public int getRearRight()
    {
        return mRearRight;
    }
    
    public int getRearCenter()
    {
        return mRearCenter;
    }
    
    public int getSoundPeriod()
    {
        return mSoundPeriod;
    }
    
    public boolean getShow()
    {
        return mShow;
    }
    
    public boolean getSoundEnabled()
    {
        return mSoundEnabled;
    }
}
