package com.autowp.arduinocan;



abstract public class Response {
    
    abstract public String toString();
    
    public static Response fromBytes(byte[] bytes) throws ArduinoCanException
    {
        if (bytes.length <= 0) {
            throw new ArduinoCanException("Invalid response: empty bytes");
        }
        switch ((char)bytes[0]) {
            case MessageResponse.PREFIX: 
                return new MessageResponse(bytes);
            default:
                return new FrameResponse(bytes);
        }
    }

}
