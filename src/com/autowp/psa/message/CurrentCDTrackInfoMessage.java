package com.autowp.psa.message;

import com.autowp.can.CanMessage;

public class CurrentCDTrackInfoMessage extends AbstractMessage {
    private byte mTrackNumber;
    private byte mCurrentMinute;
    private byte mCurrentSecond;
    private byte mSomeValue;
    private byte mTotalMinutes;
    private byte mTotalSeconds;

    public CurrentCDTrackInfoMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != 6) {
            throw new MessageException("CurrentCDTrackInfo message must be 6 bytes long");
        }
        
        mTrackNumber = data[0];
        
        mTotalMinutes = data[1];
        mTotalSeconds = data[2];
        
        mCurrentMinute = data[3];
        mCurrentSecond = data[4];

        mSomeValue = data[5];
    }

    public byte getTrackNumber() {
        return mTrackNumber;
    }

    public byte getCurrentMinute() {
        return mCurrentMinute;
    }
    
    public byte getCurrentSecond() {
        return mCurrentSecond;
    }

    public byte getSomeValue() {
        return mSomeValue;
    }
    
    public byte getTotalMinutes() {
        return mTotalMinutes;
    }
    
    public String getCurrentTime() {
        String result = "";
        if (mCurrentMinute == -1) {
            result += "--";
        } else {
            result += String.format("%02d", mCurrentMinute);
        }
        
        result += ":";
        
        if (mCurrentSecond == 0x7F) {
            result += "--";
        } else {
            result += String.format("%02d", mCurrentSecond);
        }
        
        return result;
    }
    
    public String getTotalTime() {
        String result = "";
        if (mTotalMinutes == -1) {
            result += "--";
        } else {
            result += String.format("%02d", mTotalMinutes);
        }
        
        result += ":";
        
        if (mTotalSeconds == -1) {
            result += "--";
        } else {
            result += String.format("%02d", mTotalSeconds);
        }
        
        return result;
    }
    
    public byte getTotalSeconds() {
        return mTotalSeconds;
    }
}
