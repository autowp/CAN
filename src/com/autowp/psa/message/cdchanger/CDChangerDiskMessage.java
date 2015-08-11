package com.autowp.psa.message.cdchanger;

import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;
import com.autowp.psa.CanComfort;
import com.autowp.psa.message.AbstractMessage;
import com.autowp.psa.message.MessageException;

public class CDChangerDiskMessage extends AbstractMessage {
    private static final int ID = CanComfort.ID_CD_CHANGER_DISK;
    private static final int DATA_LENGTH = 8;
    
    private static final byte DISK_BITMAP = (byte) 0xF0;
    
    private byte mDisk = 1;
    
    public CDChangerDiskMessage() {
        
    }
    
    public CDChangerDiskMessage(CanMessage message) throws MessageException
    {
        if (message.getId() != ID) {
            throw new MessageException(String.format("CDChangerDisk message have ID %s", ID));
        }
        
        byte[] data = message.getData();
        
        if (data.length != DATA_LENGTH) {
            throw new MessageException("CDChangerDisk message must be " + DATA_LENGTH + " bytes long");
        }
        
        this.assertConstBits(
            data,
            new byte[] { 
                (byte) 0xFF,
                (byte) 0xFF,
                (byte) 0xFF,
                ~DISK_BITMAP,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0xFF
            },
            new byte[] { 
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x33
            }
        );
        
        mDisk  = (byte) ((data[3] & DISK_BITMAP) >> 4);
        
        if (data[4] != 0x00 && data[4] != 0x33) {
            throw new MessageException("CDChangerDisk[4] unexpected value");
        }
        
        if (data[5] != 0x00 && data[5] != 0x03 && data[5] != 0x33) {
            throw new MessageException("CDChangerDisk[5] unexpected value");
        }
        
        if (data[6] != 0x03 && data[6] != 0x33) {
            throw new MessageException("CDChangerDisk[6] unexpected value");
        }
    }

    public byte getDisk() {
        return mDisk;
    }

    public void setDisk(byte disk) {
        mDisk = disk;
    }
    
    public CanFrame assembleFrame() throws CanFrameException 
    {
        byte data[] = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x33, 0x33};
        
        data[3] = (byte)(mDisk << 4);
        
        return new CanFrame(ID, data);
    }
}
