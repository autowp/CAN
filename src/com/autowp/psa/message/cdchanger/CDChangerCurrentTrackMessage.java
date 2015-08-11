package com.autowp.psa.message.cdchanger;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;
import com.autowp.psa.message.AbstractMessage;
import com.autowp.psa.message.MessageException;

public class CDChangerCurrentTrackMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_CD_CHANGER_CURRENT_TRACK;
    private static final int DATA_LENGTH = 7;
    
    private static final byte SECOND_BITMASK = 0x7F;
    
    private byte mTrackNumber = 1;
    private byte mCurrentMinute;
    private byte mCurrentSecond;

    public CDChangerCurrentTrackMessage() {
        
    }
    
    public CDChangerCurrentTrackMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("CDChangerCurrentTrack message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("CDChangerCurrentTrack message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 
                0x00,
                0x00,
                0x00,
                0x00,
                ~SECOND_BITMASK,
                0x00,
                (byte) 0xFF
            },
            new byte[] { 
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00
            }
        );
        
        mTrackNumber  = data[0];
        
        if (data[1] != 0x05 && data[1] != (byte)0xFF) {
            throw new MessageException("CDChangerCurrentTrack[1] unexpected value");
        }
        
        if (data[2] != 0x00 && data[2] != (byte)0xFF) {
            throw new MessageException("CDChangerCurrentTrack[2] unexpected value");
        }
        
        mCurrentMinute = data[3];
        mCurrentSecond = data[4];
        
        if (data[5] != 0x00 && data[5] != (byte)0x80) {
            throw new MessageException("CDChangerCurrentTrack[5] unexpected value");
        }
    }
    
    public byte getTrackNumber() {
        return mTrackNumber;
    }
    
    public void setTrackNumber(byte track) {
        mTrackNumber = track;
    }

    public byte getCurrentMinute() {
        return mCurrentMinute;
    }
    
    public byte getCurrentSecond() {
        return mCurrentSecond;
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
    
    public CanFrame assembleFrame() throws CanFrameException 
    {
        byte data[] = {0x00, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00};
        
        data[0] = mTrackNumber;
        data[3] = mCurrentMinute;
        data[4] = mCurrentSecond;
        
        return new CanFrame(ID, data);
    }
}
