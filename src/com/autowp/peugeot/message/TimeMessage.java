package com.autowp.peugeot.message;

import com.autowp.can.CanMessage;

public class TimeMessage extends AbstractMessage {

    private int mSecondsOfDay;
    
    private boolean mTimeFormat24;

    public TimeMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != 7) {
            throw new MessageException("TimeMessage message must be 7 bytes long");
        }
        
        int d1 = (int)data[0] & 0xFF;
        int d2 = (int)data[1] & 0xFF;
        int d3 = (int)data[2] & 0xF0;
        
        mSecondsOfDay = (d1 << 12) + (d2 << 4) + (d3 >> 4);
        
        if ((data[2] & 0x0F) != 0x00) {
            String str = String.format("%02X", data[2]);
            throw new MessageException("Unexpected TimeMessage[2] value `" + str + "`");
        }
        
        if ((data[3] & 0xFF) != 0x00) {
            String str = String.format("%02X", data[3]);
            throw new MessageException("Unexpected TimeMessage[3] value `" + str + "`");
        }
        
        if ((data[4] & 0xFF) != 0x00) {
            String str = String.format("%02X", data[4]);
            throw new MessageException("Unexpected TimeMessage[4] value `" + str + "`");
        }
        
        mTimeFormat24 = (data[5] & 0x80) != 0;
        
        if ((data[5] & 0x7F) != 0x40) {
            String str = String.format("%02X", data[5]);
            throw new MessageException("Unexpected TimeMessage[5] value `" + str + "`");
        }
        
        if ((data[6] & 0xFF) != 0x01) {
            String str = String.format("%02X", data[6]);
            throw new MessageException("Unexpected TimeMessage[6] value `" + str + "`");
        }
    }
    
    public int getSecondsOfDay()
    {
        return mSecondsOfDay;
    }
    
    public String getTimeString()
    {
        int seconds = mSecondsOfDay % 60;
        int minutesOfDay = (mSecondsOfDay - seconds) / 60;
        int minutes = minutesOfDay % 60;
        int hours = (minutesOfDay - minutes) / 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    public boolean isTimeFormat24()
    {
        return mTimeFormat24;
    }
}
