package com.autowp.peugeot.message;

import org.apache.commons.codec.binary.Hex;

import com.autowp.can.CanMessage;
import com.autowp.peugeot.CanComfort;

public class VolumeMessage extends AbstractMessage {
    
    protected int volume;
    
    protected boolean show;
    
    public VolumeMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        if (data.length != 1) {
            String str = new String(Hex.encodeHex(data));
            throw new MessageException("Unexpected length of volume message `" + str + "`");
        }
        volume = (int)data[0] & 0x1F;
        show = ((int)data[0] & 0xE0) == 0x00;
        
        if (volume < CanComfort.VOLUME_MIN) {
            String str = new String(Hex.encodeHex(data));
            throw new MessageException("Volume cannot be < " + CanComfort.VOLUME_MIN + " `" + str + "`");
        }
        
        if (volume > CanComfort.VOLUME_MAX) {
            String str = new String(Hex.encodeHex(data));
            throw new MessageException("Volume cannot be > " + CanComfort.VOLUME_MAX + " `" + str + "`");
        }
    }
    
    public int getVolume()
    {
        return volume;
    }
    
    public boolean getShow()
    {
        return show;
    }
}
