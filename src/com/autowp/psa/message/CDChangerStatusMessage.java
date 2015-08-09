package com.autowp.psa.message;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;

public class CDChangerStatusMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_CD_CHANGER_STATUS;
    private static final int DATA_LENGTH = 2;
    
    private static final byte INTRO_BITMASK = 0x20;
    private static final byte RANDOM_BITMASK = 0x04;
    
    private static final byte REPEAT_BITMASK = (byte) 0x80;
    
    private boolean mRandom;
    private boolean mRepeat;
    private boolean mIntro;
    
    public CDChangerStatusMessage() {
        
    }
    
    public CDChangerStatusMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("CDChangerStatus message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("CDChangerStatus message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 
                ~(INTRO_BITMASK | RANDOM_BITMASK),
                ~(REPEAT_BITMASK)
            },
            new byte[] { 
                (byte) 0x92,
                0x00
            }
        );
        
        mIntro  = (data[0] & INTRO_BITMASK) != 0x00;
        mRandom = (data[0] & RANDOM_BITMASK) != 0x00;
        mRepeat = (data[1] & REPEAT_BITMASK) != 0x00;
    }
    
    public boolean isRandom() {
        return mRandom;
    }
    
    public boolean isRepeat() {
        return mRepeat;
    }
    
    public boolean isIntro() {
        return mIntro;
    }
    
    public CanFrame assembleFrame() throws CanFrameException {
        byte data[] = {(byte)0x92, 0x00};
        
        data[0] = (byte) (
              ((byte) 0x92)
            | (mIntro ? INTRO_BITMASK : 0x00)
            | (mRandom ? RANDOM_BITMASK : 0x00)
        );
        data[1] = (byte) (
              0x00
            | (mRepeat ? REPEAT_BITMASK : 0x00)
        );
        
        return new CanFrame(ID, data);
    }
}
