package com.autowp.peugeot.message;

import java.util.HashMap;

import com.autowp.can.CanMessage;

public class RDSMessage extends AbstractMessage {
    
    private static final int DATA_LENGTH = 4;
    private static final byte RDS_SEARCH_BITMASK = (byte) 0x80;
    private static final byte TA_BITMASK = 0x10;
    private static final byte REG_MODE_BITMASK = 0x01;
    private static final byte PTY_MENU_BITMASK = 0x40;
    private static final byte PTY_BITMASK = (byte) 0x80;
    
    private boolean mTA;
    private boolean mShowPTYMenu;
    private byte mPTYValue;
    private boolean mPTY;
    private boolean mRDSSearchActivated;
    private boolean mREGModeActivated;
    
    private static final byte PTY_DISABLED = 0x00;
    private static final byte PTY_NEWS     = 0x01;
    private static final byte PTY_AFFAIRS  = 0x02;
    private static final byte PTY_INFO     = 0x03;
    private static final byte PTY_SPORT    = 0x04;
    private static final byte PTY_EDUCATE  = 0x05;
    private static final byte PTY_DRAMA    = 0x06;
    private static final byte PTY_CULTURE  = 0x07;
    private static final byte PTY_SCIENCE  = 0x08;
    private static final byte PTY_VARIED   = 0x09;
    private static final byte PTY_POP_M    = 0x0A;
    private static final byte PTY_ROCK_M   = 0x0B;
    private static final byte PTY_EASY_M   = 0x0C;
    private static final byte PTY_LIGHT_M  = 0x0D;
    private static final byte PTY_CLASSICS = 0x0E;
    private static final byte PTY_OTHER_M  = 0x0F;
    private static final byte PTY_WEATHER  = 0x10;
    private static final byte PTY_FINANCE  = 0x11;
    private static final byte PTY_CHILDREN = 0x12;
    private static final byte PTY_SOCIAL   = 0x13;
    private static final byte PTY_RELIGION = 0x14;
    private static final byte PTY_PHONE_IN = 0x15;
    private static final byte PTY_TRAVEL   = 0x16;
    private static final byte PTY_LEISURE  = 0x17;
    private static final byte PTY_JAZZ     = 0x18;
    private static final byte PTY_COUNTRY  = 0x19;
    private static final byte PTY_NATION_M = 0x1A;
    private static final byte PTY_OLDIES   = 0x1B;
    private static final byte PTY_FOLK_M   = 0x1C;
    private static final byte PTY_DOCUMENT = 0x1D;
    
    private static final HashMap<Byte, String> mPTYValues = new HashMap<Byte, String>(){
        private static final long serialVersionUID = 1L;
    {
        put(PTY_DISABLED, "Deactivate PTY");
        put(PTY_NEWS, "News");
        put(PTY_AFFAIRS, "Affairs");
        put(PTY_INFO, "Info");
        put(PTY_SPORT, "Sport");
        put(PTY_EDUCATE, "Educate");
        put(PTY_DRAMA, "Drama");
        put(PTY_CULTURE, "Culture");
        put(PTY_SCIENCE, "Science");
        put(PTY_VARIED, "Varied");
        put(PTY_POP_M, "Pop M");
        put(PTY_ROCK_M, "Rock M");
        put(PTY_EASY_M, "Easy M");
        put(PTY_LIGHT_M, "Light M");
        put(PTY_CLASSICS, "Classics");
        put(PTY_OTHER_M, "Other M");
        put(PTY_WEATHER, "Weather");
        put(PTY_FINANCE, "Finance");
        put(PTY_CHILDREN, "Children");
        put(PTY_SOCIAL, "Social");
        put(PTY_RELIGION, "Religion");
        put(PTY_PHONE_IN, "Phone In");
        put(PTY_TRAVEL, "Travel");
        put(PTY_LEISURE, "Leisure");
        put(PTY_JAZZ, "Jazz");
        put(PTY_COUNTRY, "Country");
        put(PTY_NATION_M, "Nation M");
        put(PTY_OLDIES, "Oldies");
        put(PTY_FOLK_M, "Folk M");
        put(PTY_DOCUMENT, "Document");
    }};

    public RDSMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("RDS message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { (byte) 0x6E, 0x3F, 0x00, (byte) 0xFF}, 
            new byte[] { (byte) 0x00, 0x00, 0x00,        0x00}
        );
        
        mRDSSearchActivated = (data[0] & RDS_SEARCH_BITMASK) != 0x00;
        mTA = (data[0] & TA_BITMASK) != 0x00;
        mREGModeActivated = (data[0] & REG_MODE_BITMASK) != 0x00;
        
        mPTY = (data[1] & PTY_BITMASK) != 0x00;
        mShowPTYMenu = (data[1] & PTY_MENU_BITMASK) != 0x00;
        mPTYValue = data[2];
    }

    public boolean isTA() {
        return mTA;
    }

    public boolean isShowPTYMenu() {
        return mShowPTYMenu;
    }

    public byte getPTYValue() {
        return mPTYValue;
    }
    
    public String getPTYValueString() {
        return mPTYValues.get(mPTYValue);
    }

    public boolean isPTY() {
        return mPTY;
    }

    public boolean isRDSSearchActivated() {
        return mRDSSearchActivated;
    }
    
    public boolean isREGModeActivated() {
        return mREGModeActivated;
    }
}
