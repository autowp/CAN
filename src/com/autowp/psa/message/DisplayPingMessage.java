package com.autowp.psa.message;

import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;

public class DisplayPingMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_DISPLAY_PING;
    private static final int DATA_LENGTH = 8;
    
    public DisplayPingMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("DisplayPing message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("DisplayPing message must be " + DATA_LENGTH + " bytes long");
        }
    }
}
