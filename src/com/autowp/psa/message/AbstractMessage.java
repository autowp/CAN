package com.autowp.psa.message;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;

public abstract class AbstractMessage {
    protected void assertConstBits(byte[] data, byte[] mask, byte[] value) throws MessageException
    {
        if (mask.length != value.length || data.length != value.length) {
            throw new MessageException("Mask and values count must be the same");
        }
        
        for (int i=0; i<mask.length; i++) {
            if ((data[i] & mask[i]) != value[i]) {
                String className = this.getClass().getName();
                String str = String.format("Unexpected %s[%d] value: %02X & %02X != %02X", className, i, data[i],  mask[i], value[i]);
                throw new MessageException(str);
            }
        }
    }
    
    public CanFrame assembleFrame() throws CanFrameException 
    {
        return null;
    }
}
