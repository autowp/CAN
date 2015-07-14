package com.autowp.psa.message;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;

public class BSIVINMessage extends AbstractMessage {
    private static final int DATA_LENGTH = 8;
    private static final int ID = CanComfort.ID_VIN;
    
    private String mVIN = null;
    
    public BSIVINMessage()
    {
        
    }

    public BSIVINMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException("BSIVINMessage message must have id " + ID);
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("BSIVINMessage message must be " + DATA_LENGTH + " bytes long");
        }
        
        mVIN = new String(data);
    }

    public String getVIN() {
        return mVIN;
    }

    public void setVIN(String VIN) throws MessageException {
        if (VIN.length() != DATA_LENGTH) {
            throw new MessageException("BSIVINMessage VIN must be " + DATA_LENGTH + " bytes long");
        }
        
        mVIN = VIN;
    }
    
    public CanFrame assembleFrame() throws CanFrameException
    {
        return new CanFrame(ID, mVIN.getBytes());
    }
}
