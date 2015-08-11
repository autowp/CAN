package com.autowp.psa.message.cdchanger;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;
import com.autowp.psa.message.AbstractMessage;
import com.autowp.psa.message.MessageException;

public class CDChangerPingMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_CD_CHANGER_PING;
    private static final int DATA_LENGTH = 8;
    
    public CDChangerPingMessage() {
        
    }
    
    public CDChangerPingMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("CDChangerPing message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("CDChangerPing message must be " + DATA_LENGTH + " bytes long");
        }
    }
    
    public CanFrame assembleFrame() throws CanFrameException 
    {
        byte data[] = {0x31, 0x07, 0x01, 0x05, 0x02, 0x04, 0x20, 0x09};
        
        return new CanFrame(ID, data);
    }
}
