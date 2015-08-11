package com.autowp.psa.message.cdchanger;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;
import com.autowp.psa.message.AbstractMessage;
import com.autowp.psa.message.MessageException;

public class CDChangerWelcomeMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_CD_CHANGER_WELCOME;
    private static final int DATA_LENGTH = 8;
    
    public CDChangerWelcomeMessage() {
        
    }
    
    public CDChangerWelcomeMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("CDChangerWelcome message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("CDChangerWelcome message must be " + DATA_LENGTH + " bytes long");
        }
    }
    
    public CanFrame assemble() throws CanFrameException 
    {
        byte data[] = {0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        
        return new CanFrame(ID, data);
    }
}
