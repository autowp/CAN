package com.autowp.psa.message.cdchanger;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;
import com.autowp.psa.message.AbstractMessage;
import com.autowp.psa.message.MessageException;

public class CDChangerTracksCountMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_CD_CHANGER_TRACKS_COUNT;
    private static final int DATA_LENGTH = 5;
    private byte mCount = 1;
    
    public CDChangerTracksCountMessage() {
        
    }
    
    public CDChangerTracksCountMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("CDChangerTracksCount message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("CDChangerTracksCount message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 
                0x00,
                (byte) 0xFF,
                (byte) 0xFF,
                (byte) 0xFF,
                (byte) 0xFF,
            },
            new byte[] { 
                0x00,
                0x58,
                0x00,
                0x00,
                0x00
            }
        );
        
        mCount  = data[0];
    }

    public byte getCount() {
        return mCount;
    }

    public void setCount(byte count) {
        mCount = count;
    }
    
    public CanFrame assembleFrame() throws CanFrameException 
    {
        byte data[] = {mCount, 0x58, 0x00, 0x00, 0x00};
        
        return new CanFrame(ID, data);
    }
}
