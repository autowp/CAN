package com.autowp.peugeot.message;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.peugeot.CanComfort;

public class BSIInfoWindowMessage extends AbstractMessage {
    private static final int DATA_LENGTH = 8;
    private static final int ID = CanComfort.ID_BSI_INFO_WINDOW;
    
    public static final byte ACTION_SHOW = (byte) 0x80;
    public static final byte ACTION_HIDE = (byte) 0x7F;
    public static final byte ACTION_CLEAR = (byte) 0xFF;
    
    private byte mAction = ACTION_CLEAR;
    private byte mCode;
    
    public BSIInfoWindowMessage()
    {
        
    }

    public BSIInfoWindowMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException("BSIStatusMessage message must have id " + ID);
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("BSIInfoWindowMessage message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 },
            new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }
        );
        
        mAction = data[0];
        
        switch (mAction) {
            case ACTION_SHOW:
            case ACTION_HIDE:
            case ACTION_CLEAR:
                break;
            default:
                throw new MessageException("BSIInfoWindowMessage action unexpected `" + mAction + "`");
        }
        
        mCode = data[1];
    }

    public byte getCode() {
        return mCode;
    }

    public void setCode(byte code) {
        mCode = code;
    }
    
    public byte getAction() {
        return mAction;
    }
    
    public void setAction(byte action) {
        mAction = action;
    }
    
    public CanFrame assembleFrame() throws CanFrameException
    {
        byte data[] = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        
        data[0] = mAction;
        data[1] = mCode;
        
        if (mAction == ACTION_SHOW) {
            data[2] = (byte) 0x80;
        }
        
        return new CanFrame(ID, data);
    }
}
