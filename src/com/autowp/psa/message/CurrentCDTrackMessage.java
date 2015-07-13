package com.autowp.psa.message;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

import com.autowp.can.CanMessage;

public class CurrentCDTrackMessage extends AbstractMessage {
    
    Track track;
    
    public CurrentCDTrackMessage(CanMessage message) throws MessageException
    {
        byte[] data = message.getData();
        if (data.length == 5) {
            final byte[] emptyData = new byte[] {0x20, 0x00, 0x00, 0x00, 0x00};
            if (!Arrays.equals(data, emptyData)) {
                String str = new String(Hex.encodeHex(data));
                throw new MessageException("Unexpected data `" + str + "`");
            }
            
            track = new Track("", "");

        } else {
            
            if (data.length < 3) {
                String str = new String(Hex.encodeHex(data));
                throw new MessageException("Message too short `" + str + "`");
            }
            
            if (!(data[0] == 0x20 && data[1] == 0x00)) {
                String str = new String(Hex.encodeHex(data));
                throw new MessageException("Unexpected data `" + str + "`");
            }
            
            if ((data[2] & 0xEF) != 0x48) {
                String str = new String(Hex.encodeHex(data));
                throw new MessageException("Unexpected data `" + str + "`");
            }
            
            boolean trackAuthorExists = (data[2] & 0x10) == 0x10;
            int textDataOffset = 4;
            byte[] textData = new byte[data.length - textDataOffset];
            System.arraycopy(data, textDataOffset, textData, 0, textData.length);
            track = new Track();
            try {
                track.readFromBytes(textData, trackAuthorExists, true);
            } catch (MessageException e) {
                throw new MessageException(e.getMessage());
            }
        }
    }
    
    public Track getTrack()
    {
        return track;
    }
}
