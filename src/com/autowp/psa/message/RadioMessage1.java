package com.autowp.psa.message;

import com.autowp.can.CanMessage;

public class RadioMessage1 extends AbstractMessage {
    private static final int DATA_LENGTH = 5;
    
    private static final byte UNKNOWN1_BITMASK = (byte) 0xC0;
    private static final byte TRACK_INTRO_BITMASK = 0x20;
    private static final byte UNKNOWN2_BITMASK = 0x10;
    private static final byte RANDOM_PLAY_BITMASK = 0x04;
    private static final byte UNKNOWN3_BITMASK = 0x03;
    
    private static final byte UNKNOWN4_BITMASK = (byte) 0xC0;
    private static final byte ALT_FREQUENCIES_BITMASK = 0x20;
    private static final byte UNKNOWN5_BITMASK = 0x1F;
    
    private static final byte REG_MODE_BITMASK = (byte) 0x80;
    
    private static final byte UNKNOWN6_BITMASK = (byte) 0x80;
    private static final byte RADIO_TEXT_BITMASK = 0x20;

    private boolean mTrackIntro;

    private boolean mRandomPlay;

    private boolean mAltFreqencies;

    private boolean mRadioText;

    private boolean mRegMode;
    
    private byte mUnknown1;

    private boolean mUnknown2;

    private byte mUnknown3;
    
    private byte mUnknown4;
    
    private byte mUnknown5;
    
    private byte mUnknown6;

    public RadioMessage1(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("RadioMessage1 message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 
                ~(UNKNOWN1_BITMASK | TRACK_INTRO_BITMASK | UNKNOWN2_BITMASK | RANDOM_PLAY_BITMASK | UNKNOWN3_BITMASK),
                (byte) 0xFF,
                ~(ALT_FREQUENCIES_BITMASK | UNKNOWN4_BITMASK | UNKNOWN5_BITMASK),
                ~REG_MODE_BITMASK,
                ~(UNKNOWN6_BITMASK | RADIO_TEXT_BITMASK)
            }, 
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x00,
                0x00
            }
        );
        
        mUnknown1 = (byte) ((byte) (data[0] & UNKNOWN1_BITMASK) >> 6);
        mTrackIntro = (data[0] & TRACK_INTRO_BITMASK) != 0x00;
        mUnknown2 = (data[0] & UNKNOWN2_BITMASK) != 0x00;
        mRandomPlay = (data[0] & RANDOM_PLAY_BITMASK) != 0x00;
        mUnknown3 = (byte) (data[0] & UNKNOWN3_BITMASK);
        
        mUnknown4 = (byte) (data[2] & UNKNOWN4_BITMASK);
        mAltFreqencies = (data[2] & ALT_FREQUENCIES_BITMASK) != 0x00;
        mUnknown5 = (byte) (data[2] & UNKNOWN5_BITMASK);
        
        mRegMode = (data[3] & REG_MODE_BITMASK) != 0x00;
        
        mUnknown6 = (byte) (data[4] & UNKNOWN6_BITMASK);
        mRadioText = (data[4] & RADIO_TEXT_BITMASK) != 0x00;
    }

    public boolean isTrackIntro() {
        return mTrackIntro;
    }

    public void setTrackIntro(boolean mTrackIntro) {
        this.mTrackIntro = mTrackIntro;
    }

    public boolean isRandomPlay() {
        return mRandomPlay;
    }

    public void setRandomPlay(boolean mRandomPlay) {
        this.mRandomPlay = mRandomPlay;
    }

    public boolean isAltFreqencies() {
        return mAltFreqencies;
    }

    public void setAltFreqencies(boolean mAltFreqencies) {
        this.mAltFreqencies = mAltFreqencies;
    }

    public boolean isRadioText() {
        return mRadioText;
    }

    public void setRadioText(boolean mRadioText) {
        this.mRadioText = mRadioText;
    }

    public boolean isRegMode() {
        return mRegMode;
    }

    public void setRegMode(boolean mRegMode) {
        this.mRegMode = mRegMode;
    }

    public boolean isUnknown2() {
        return mUnknown2;
    }

    public void setUnknown2(boolean mUnknown2) {
        this.mUnknown2 = mUnknown2;
    }

    public byte getUnknown3() {
        return mUnknown3;
    }

    public void setUnknown3(byte mUnknown) {
        this.mUnknown3 = mUnknown;
    }

    public byte getUnknown1() {
        return mUnknown1;
    }

    public void setUnknown1(byte mUnknown1) {
        this.mUnknown1 = mUnknown1;
    }
    
    public byte getUnknown4() {
        return mUnknown4;
    }

    public void setUnknown4(byte unknown4) {
        mUnknown4 = unknown4;
    }

    public byte getUnknown5() {
        return mUnknown5;
    }

    public void setUnknown5(byte mUnknown5) {
        this.mUnknown5 = mUnknown5;
    }

    public byte getUnknown6() {
        return mUnknown6;
    }

    public void setUnknown6(byte mUnknown6) {
        this.mUnknown6 = mUnknown6;
    }
}
