package com.autowp.arduinocan;

import com.autowp.can.CanFrame;

public class FrameResponse extends Response {
    CanFrame mFrame;
    
    public FrameResponse(byte[] bytes) throws ArduinoCanException 
    {
        mFrame = ArduinoCan.parseArduinoCanMessage(bytes);
    }

    @Override
    public String toString() {
        return mFrame.toString();
    }
    
    public CanFrame getFrame()
    {
        return mFrame;
    }
}
