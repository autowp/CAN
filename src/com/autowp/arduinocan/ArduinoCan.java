package com.autowp.arduinocan;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.autowp.HexHelper;
import com.autowp.HexHelper.HexException;
import com.autowp.can.CanAdapter;
import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;





public abstract class ArduinoCan extends CanAdapter {
    
    protected static final int MIN_DATA_LENGTH = 1;
    protected static final int MAX_DATA_LENGTH = 8;
    protected static final int MIN_DATA_HEX_LENGTH = MIN_DATA_LENGTH * 2;
    protected static final int MAX_DATA_HEX_LENGTH = MAX_DATA_LENGTH * 2;
    
    protected static final char ID_SEPARATOR = ' ';
    protected static final char ID_SEPARATOR_LENGTH = 1;
    
    protected static final int ID_HEX_LENGTH = 3;
    
    protected static final int DATA_OFFSET = ID_HEX_LENGTH + ID_SEPARATOR_LENGTH;
    
    protected static final int LINE_MIN_LENGTH = DATA_OFFSET + MIN_DATA_HEX_LENGTH;
    protected static final int LINE_MAX_LENGTH = DATA_OFFSET + MAX_DATA_HEX_LENGTH;
    
    protected static final int ARDUINO_USB_VENDOR_ID = 0x0403;
    
    protected static final int BAUDRATE = 115200;
    
    public interface OnResponseReceivedListener {
        public void handleResponseReceivedEvent(Response response);
    }
    
    private List<OnResponseReceivedListener> mResponseReceivedListeners = new ArrayList<OnResponseReceivedListener>();
    
    protected static CanFrame parseArduinoCanMessage(byte bytes[]) throws ArduinoCanException
    {
        CanFrame frame = null;
        
        try {
            
            if (bytes.length < LINE_MIN_LENGTH) {
                throw new ArduinoCanException("Line error: line too short: " + bytes.length);
            }
            
            if (bytes.length > LINE_MAX_LENGTH) {
                throw new ArduinoCanException("Line error: line too long: " + bytes.length);
            }
            
            if (bytes[ID_HEX_LENGTH] != ID_SEPARATOR) {
                throw new ArduinoCanException("Line error: separator not found");
            }
            
            byte[] idBytes = new byte[ID_HEX_LENGTH];
            System.arraycopy(bytes, 0, idBytes, 0, ID_HEX_LENGTH);
            
            int dataLength = bytes.length - DATA_OFFSET;
            byte[] dataBytes = new byte[dataLength];
            System.arraycopy(bytes, DATA_OFFSET, dataBytes, 0, dataLength);
            
            int id = com.autowp.HexHelper.canIdFromHex(idBytes);
            
            frame = new CanFrame(id, com.autowp.HexHelper.bytesFromHex(dataBytes));
            
        } catch (CanFrameException e) {
            throw new ArduinoCanException("CanFrame error: " + e.getMessage());
        } catch (HexException e) {
            throw new ArduinoCanException("Hex error: " + e.getMessage());
        }
        
        return frame;
    }

    protected static byte[] buildArduinoCanMessage(CanFrame frame) throws ArduinoCanException {
        String line = HexHelper.canIdToHex(frame.getId()) + ID_SEPARATOR + HexHelper.bytesToHex(frame.getData());
        
        byte[] data;
        try {
            data = line.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new ArduinoCanException("Unsupported encoding");
        }
        return data;
    }
    
    protected void processInputLine(byte buffer[]) throws ArduinoCanException
    {
        Response response = Response.fromBytes(buffer);
        fireResponseReceivedEvent(response);
    }
    
    public synchronized void addEventListener(OnResponseReceivedListener listener) 
    {
        mResponseReceivedListeners.add(listener);
    }
    
    public synchronized void removeEventListener(OnResponseReceivedListener listener)   
    {
        mResponseReceivedListeners.remove(listener);
    }
    
    protected synchronized void fireResponseReceivedEvent(Response response) 
    { 
        Iterator<OnResponseReceivedListener> i = mResponseReceivedListeners.iterator();
        while(i.hasNext())  {
            ((OnResponseReceivedListener) i.next()).handleResponseReceivedEvent(response);
        }
        
        if (response instanceof FrameResponse) {
            CanFrame frame = ((FrameResponse) response).getFrame();
            fireFrameReceivedEvent(frame);
        }
    }
}