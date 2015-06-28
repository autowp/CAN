package com.autowp.peugeot.message;

import com.autowp.can.CanMessage;

public class RadioKeypadMessage extends AbstractMessage {
    
    private static final int DATA_LENGTH = 6;
    
    private static final byte MENU_BITMASK = 0x40;
    private static final byte CLIM_BITMASK = 0x01;
    
    private static final byte TRIP_BITMASK = 0x40;
    private static final byte AUDIO_BITMASK = 0x01;
    
    private static final byte OK_BITMASK = 0x40;
    private static final byte ESC_BITMASK = 0x10;
    private static final byte DARK_BITMASK = 0x04;
    
    private static final byte UP_BITMASK = 0x40;
    private static final byte DOWN_BITMASK = 0x10;
    private static final byte RIGHT_BITMASK = 0x04;
    private static final byte LEFT_BITMASK = 0x01;

    private boolean mMenu;

    private boolean mClim;

    private boolean mTrip;

    private boolean mAudio;

    private boolean mOk;

    private boolean mESC;

    private boolean mDark;

    private boolean mUp;

    private boolean mDown;

    private boolean mRight;

    private boolean mLeft;

    public RadioKeypadMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("RadioKeypad message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] {(byte) 0xBE, (byte) 0xBE, (byte) 0xAB, (byte) 0xFF, (byte) 0xFF, (byte) 0xAA}, 
            new byte[] {       0x00,        0x00,        0x00,        0x00,        0x00,        0x00}
        );
        
        mMenu  = (data[0] & MENU_BITMASK) != 0x00;
        mClim  = (data[0] & CLIM_BITMASK) != 0x00;
        
        mTrip  = (data[1] & TRIP_BITMASK) != 0x00;
        mAudio  = (data[1] & AUDIO_BITMASK) != 0x00;
        
        mOk  = (data[2] & OK_BITMASK) != 0x00;
        mESC  = (data[2] & ESC_BITMASK) != 0x00;
        mDark  = (data[2] & DARK_BITMASK) != 0x00;
        
        mUp  = (data[5] & UP_BITMASK) != 0x00;
        mDown  = (data[5] & DOWN_BITMASK) != 0x00;
        mRight  = (data[5] & RIGHT_BITMASK) != 0x00;
        mLeft  = (data[5] & LEFT_BITMASK) != 0x00;
    }

    public boolean isMenu() {
        return mMenu;
    }

    public boolean isClim() {
        return mClim;
    }

    public boolean isTrip() {
        return mTrip;
    }

    public boolean isAudio() {
        return mAudio;
    }

    public boolean isOk() {
        return mOk;
    }

    public boolean isESC() {
        return mESC;
    }

    public boolean isDark() {
        return mDark;
    }

    public boolean isUp() {
        return mUp;
    }

    public boolean isDown() {
        return mDown;
    }

    public boolean isRight() {
        return mRight;
    }

    public boolean isLeft() {
        return mLeft;
    }
}