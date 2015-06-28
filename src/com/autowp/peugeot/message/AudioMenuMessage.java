package com.autowp.peugeot.message;

import java.util.HashMap;

import com.autowp.can.CanMessage;

public class AudioMenuMessage extends AbstractMessage {

    private int mSideBalance;
    private int mBalance;
    private boolean mShowSideBalance;
    private boolean mShowTreble;
    private boolean mShowBass;
    private boolean mShowBalance;
    private int mBass;
    private int mTreble;
    private boolean mShowLoudnessCorrection;
    private boolean mLoudnessCorrection;
    private boolean mShowAutomaticVolume;
    private boolean mAutomaticVolume;
    private boolean mShowMusicalAmbiance;
    private int mMusicalAmbiance;
    
    private static final HashMap<Integer, String> mMusicalAmbianceNames = new HashMap<Integer, String>(){
        private static final long serialVersionUID = 1L;
    {
        put(0x03, "None");
        put(0x07, "Classical");
        put(0x0B, "Jazz-Blues");
        put(0x0F, "Pop-Rock");
        put(0x13, "Vocal");
        put(0x17, "Techno");
    }};
    
    private static final HashMap<Integer, Integer> mBalanceValues = new HashMap<Integer, Integer>(){
        private static final long serialVersionUID = 1L;
    {
        put(0b0110110, -9);
        put(0b0110111, -8);
        put(0b0111000, -7);
        put(0b0111001, -6);
        put(0b0111010, -5);
        put(0b0111011, -4);
        put(0b0111100, -3);
        put(0b0111101, -2);
        put(0b0111110, -1);
        put(0b0111111,  0);
        put(0b1000000,  1);
        put(0b1000001,  2);
        put(0b1000010,  3);
        put(0b1000011,  4);
        put(0b1000100,  5);
        put(0b1000101,  6);
        put(0b1000110,  7);
        put(0b1000111,  8);
        put(0b1001000,  9);
    }};

    public AudioMenuMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != 7) {
            throw new MessageException("AudioMenu message must be 7 bytes long");
        }
        
        mShowSideBalance = (data[0] & 0x80) != 0;
        int sideBalance = data[0] & 0x7F;
        if (!mBalanceValues.containsKey(sideBalance)) {
            String str = String.format("%02X", data[3]);
            throw new MessageException("Unexpected SideBalance value `" + str + "`");
        }
        mSideBalance = mBalanceValues.get(sideBalance);
        
        mShowBalance = (data[1] & 0x80) != 0;
        int balance = data[1] & 0x7F;
        if (!mBalanceValues.containsKey(balance)) {
            throw new MessageException("Unexpected Balance value");
        }
        mBalance = mBalanceValues.get(balance);
        
        mShowBass = (data[2] & 0x80) != 0;
        int bass = data[2] & 0x7F;
        if (!mBalanceValues.containsKey(bass)) {
            throw new MessageException("Unexpected Bass value");
        }
        mBass = mBalanceValues.get(bass);
        
        if (data[3] != 0x3F) {
            String str = String.format("%02X", data[3]);
            throw new MessageException("Unexpected AudioMenu[3] value `" + str + "`");
        }
        
        mShowTreble = (data[4] & 0x80) != 0;
        int treble = data[4] & 0x7F;
        if (!mBalanceValues.containsKey(treble)) {
            throw new MessageException("Unexpected Bass value");
        }
        mTreble = mBalanceValues.get(treble);
        
        mShowLoudnessCorrection = (data[5] & 0x80) != 0;
        mLoudnessCorrection = (data[5] & 0x40) != 0;
        
        if ((data[5] & 0x28) != 0x00) {
            String str = String.format("%02X", data[5]);
            throw new MessageException("Unexpected AudioMenu[5] value `" + str + "`");
        }
        
        mShowAutomaticVolume = (data[5] & 0x10) != 0;
        int automaticVolume = data[5] & 0x07;
        switch (automaticVolume) {
            case 0x07:
                mAutomaticVolume = true;
                break;
                
            case 0x00:
                mAutomaticVolume = false;
                break;
                
            default:
                String str = String.format("%02X", automaticVolume);
                throw new MessageException("Unexpected automatic volume value `" + str + "`");
        }
        
        if ((data[6] & 0xA0) != 0x00) {
            throw new MessageException("Unexpected AudioMenu[6] value");
        }
        
        mShowMusicalAmbiance = (data[6] & 0x40) != 0;
        mMusicalAmbiance = data[6] & 0x1F;
        
        if (!mMusicalAmbianceNames.containsKey(mMusicalAmbiance)) {
            String str = String.format("%02X", mMusicalAmbiance);
            throw new MessageException("Unexpected Musical Ambiance value `" + str + "`");
        }
    }

    public int getSideBalance() {
        return mSideBalance;
    }

    public int getBalance() {
        return mBalance;
    }

    public boolean isShowSideBalance() {
        return mShowSideBalance;
    }

    public boolean isShowTreble() {
        return mShowTreble;
    }
    
    public boolean isShowBass() {
        return mShowBass;
    }

    public boolean isShowBalance() {
        return mShowBalance;
    }

    public int getBass() {
        return mBass;
    }

    public int getTreble() {
        return mTreble;
    }

    public boolean isShowLoudnessCorrection() {
        return mShowLoudnessCorrection;
    }

    public boolean isLoudnessCorrection() {
        return mLoudnessCorrection;
    }

    public boolean isShowAutomaticVolume() {
        return mShowAutomaticVolume;
    }
    
    public boolean isAutomaticVolume() {
        return mAutomaticVolume;
    }

    public boolean isShowMusicalAmbiance() {
        return mShowMusicalAmbiance;
    }
    
    public int getMusicalAmbiance()
    {
        return mMusicalAmbiance;
    }
    
    public String getMusicalAmbianceName()
    {
        return mMusicalAmbianceNames.get(mMusicalAmbiance);
    }
}
