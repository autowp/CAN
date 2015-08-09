package com.autowp.psa.message;

import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;

public class RadioCDChangerCommandMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_RADIO_CD_CHANGER_COMMAND;
    private static final int DATA_LENGTH = 5;
    
    private static final byte ENABLE_BITMASK = (byte) 0x80;
    private static final byte TRACK_BACK_BITMASK = 0x40;
    private static final byte TRACK_FORWARD_BITMASK = 0x20;
    private static final byte FAST_BITMASK = 0x10;
    private static final byte REWIND_TRACK_BITMASK = 0x08;
    private static final byte INTRO_BITMASK = 0x04;
    private static final byte ENABLE_PLAYING_BITMASK = 0x02;
    private static final byte UNKNOWN0_BITMASK = 0x01;
    
    private static final byte RANDOM_BITMASK = 0x01;
    private static final byte REPEAT_BITMASK = 0x08;
    
    
    private byte mUnknown0;
    private byte mGotoDisk;
    private byte mGotoTrack;
    private boolean mRandom;
    private boolean mRepeat;
    private boolean mEnable;
    private boolean mEnablePlaying;
    private boolean mTrackBack;
    private boolean mTrackForward;
    private boolean mRewindTrack;
    private boolean mIntro;
    private boolean mFastForward;
    private boolean mFastBackward;
    
    public RadioCDChangerCommandMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("RadioCDChangerCommand message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("RadioCDChangerCommand message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 
                ~(ENABLE_BITMASK | TRACK_BACK_BITMASK | TRACK_FORWARD_BITMASK | FAST_BITMASK | REWIND_TRACK_BITMASK | INTRO_BITMASK | ENABLE_PLAYING_BITMASK | UNKNOWN0_BITMASK), 
                ~(RANDOM_BITMASK | REPEAT_BITMASK), 
                0x00, (byte) 
                0xFF, 
                0x00 
            }, 
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x00,
                0x00
            }
        );
        
        mEnable = (data[0] & ENABLE_BITMASK) != 0x00;
        mEnablePlaying = (data[0] & ENABLE_PLAYING_BITMASK) != 0x00;
        mTrackBack = (data[0] & TRACK_BACK_BITMASK) != 0x00;
        mTrackForward = (data[0] & TRACK_FORWARD_BITMASK) != 0x00;
        mIntro = (data[0] & INTRO_BITMASK) != 0x00;
        mRewindTrack = (data[0] & REWIND_TRACK_BITMASK) != 0x00;
        mUnknown0 = (byte) (data[0] & UNKNOWN0_BITMASK);
        mFastForward = ((data[0] & FAST_BITMASK) != 0x00) && !mEnablePlaying && mIntro;
        mFastBackward = ((data[0] & FAST_BITMASK) == 0x00) && !mEnablePlaying && mIntro;
                
        mRandom = (data[1] & RANDOM_BITMASK) != 0x00;
        mRepeat = (data[1] & REPEAT_BITMASK) != 0x00;
        
        mGotoDisk = data[2];
        
        mGotoTrack = data[4];
    }
    
    public byte getUnknown0() {
        return mUnknown0;
    }
    
    public byte getGotoDisk() {
        return mGotoDisk;
    }
    
    public byte getGotoTrack() {
        return mGotoTrack;
    }
    
    public boolean isRandom() {
        return mRandom;
    }
    
    public boolean isRepeat() {
        return mRepeat;
    }
    
    public boolean isEnable() {
        return mEnable;
    }
    
    public boolean isEnablePlaying() {
        return mEnablePlaying;
    }
    
    public boolean isTrackBack() {
        return mTrackBack;
    }
    
    public boolean isTrackForward() {
        return mTrackForward;
    }
    
    public boolean isRewindTrack() {
        return mRewindTrack;
    }
    
    public boolean isIntro() {
        return mIntro;
    }
    
    public boolean isFastForward() {
        return mFastForward;
    }
    
    public boolean isFastBackward() {
        return mFastBackward;
    }
}
