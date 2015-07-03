package com.autowp.peugeot.message;

import com.autowp.can.CanMessage;

public class DisplayConditioningMessage extends AbstractMessage {
    
    private static final int DATA_LENGTH = 1;
    private static final byte DEFAULT = 0x10;
    private static final byte LH_RH_CONTROL = 0x00;
    private static final byte AC_OFF = 0x18;
    
    private boolean mIsDefault;
    private boolean mIsLHRHControl;
    private boolean mIsAcOff;

    public DisplayConditioningMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("DisplayConditioning message must be " + DATA_LENGTH + " bytes long");
        }
        
        mIsDefault = false;
        mIsLHRHControl = false;
        mIsAcOff = false;
        
        switch (data[0]) {
            case DEFAULT:
                mIsDefault = true;
                break;
            case LH_RH_CONTROL:
                mIsLHRHControl = true;
                break;
            case AC_OFF:
                mIsAcOff = true;
                break;
            default:
                String str = String.format("%02X", data[0]);
                throw new MessageException("Unexpected DisplayConditioningMessage[0] value `" + str + "`");
        }

    }
    
    public boolean isDefault()
    {
        return mIsDefault;
    }
    
    public boolean isLHRHControl()
    {
        return mIsLHRHControl;
    }
    
    public boolean isAcOff()
    {
        return mIsAcOff;
    }
}
