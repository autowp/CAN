package com.autowp.peugeot.message;

import com.autowp.can.CanMessage;

public class CurrentCDTrackInfoMessage extends AbstractMessage {
    private int mTrackNumber;
    private int mMinutes;
    private int mSeconds;
    private int mSomeValue1;
    private int mSomeValue2;

    public CurrentCDTrackInfoMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != 6) {
            throw new MessageException("CurrentCDTrackInfo message must be 6 bytes long");
        }
        
        mTrackNumber = data[0];
        
        if (data[1] != 0x00) {
            String str = String.format("%02X", data[1]);
            throw new MessageException("Unexpected CurrentCDTrackInfo[1] value `" + str + "`");
        }
        
        if (data[2] != 0x00) {
            String str = String.format("%02X", data[2]);
            throw new MessageException("Unexpected CurrentCDTrackInfo[2] value `" + str + "`");
        }
        
        mMinutes = data[3];
        
        mSeconds = data[4];
        
        if (data[5] != 0x00) {
            String str = String.format("%02X", data[5]);
            throw new MessageException("Unexpected CurrentCDTrackInfo[5] value `" + str + "`");
        }
    }

    public int getTrackNumber() {
        return mTrackNumber;
    }

    public int getMinutes() {
        return mMinutes;
    }
    
    public int getSeconds() {
        return mSeconds;
    }
}
