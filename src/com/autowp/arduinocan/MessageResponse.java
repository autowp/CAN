package com.autowp.arduinocan;

public class MessageResponse extends Response {
    
    public static final char PREFIX = '#';
    
    String mMessage = "";
    
    public MessageResponse(byte[] bytes) throws ArduinoCanException
    {
        if (bytes.length <= 0) {
            throw new ArduinoCanException("Empty bytes");
        }
        
        if (bytes[0] != PREFIX) {
            throw new ArduinoCanException("Prefix not found");
        }
        
        mMessage = new String(bytes);
    }

    @Override
    public String toString() {
        return mMessage;
    }

}