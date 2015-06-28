package com.autowp.peugeot;

import java.nio.charset.Charset;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.autowp.can.CanClient;
import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.can.CanMessage;

public class CanComfort {
    public static final int SPEED = 125; // kbit
    
    public static final int ID_IGNITION = 0x036;
    public static final int ID_CURRENT_CD_TRACK = 0x0A4;
    public static final int ID_PARKTRONIC = 0x0E1;
    public static final int ID_TRACK_LIST = 0x125;
    public static final int ID_VOLUME = 0x1A5;
    public static final int ID_AUDIO_MENU = 0x1E5;
    public static final int ID_COLUMN_KEYPAD = 0x21F;
    public static final int ID_VIN = 0x2B6;
    public static final int ID_CURRENT_CD_TRACK_INFO = 0x3A5;
    public static final int ID_TIME = 0x3F6;
    
    public static final int IGNITION_PERIOD = 200;
    
    public static final int VIN_DELAY = 300;
    public static final int VIN_LENGTH = 8;
    
    public static final int TRACK_LIST_TRACK_AUTHOR_LENGTH = 20;
    public static final int TRACK_LIST_TRACK_NAME_LENGTH = 20;
    
    public static final int VOLUME_MIN = 0;
    public static final int VOLUME_MAX = 30;
    
    public static final Charset charset = Charset.forName("ISO-8859-1");
    
    public static CanFrame ignitionFrame() throws DecoderException, CanFrameException
    {
        byte[] bytes = Hex.decodeHex("0E00000F010000A0".toCharArray());
        return new CanFrame(CanComfort.ID_IGNITION, bytes);
    }
    
    public static CanFrame vinFrame(String vin) throws CanComfortException, CanFrameException
    {
        int length = vin.length();
        if (length < VIN_LENGTH) {
            throw new CanComfortException("Vin require at least " + VIN_LENGTH + " last digits");
        }
        
        String lastDigits = vin.substring(length - VIN_LENGTH, length);
        return new CanFrame(CanComfort.ID_VIN, lastDigits.getBytes(charset));
    }
    
    public static void emulateBSIIgnition(CanClient client, String vin) throws CanComfortException 
    {
        try {
            client.addTimerTaskFrame(ignitionFrame(), 0, IGNITION_PERIOD);
            client.sendDelayedFrame(vinFrame(vin), VIN_DELAY);
        } catch (DecoderException | CanFrameException e) {
            throw new CanComfortException(e.getMessage());
        }
    }
    
    public static String getMessageComment(CanMessage message)
    {
        switch (message.getId()) {
            case ID_IGNITION:
                return "Ignition";
                
            case ID_VIN:
                return "VIN";
                
            case ID_TRACK_LIST:
                return "Track list";
                
            case ID_VOLUME:
                return "Volume";
                
            case ID_CURRENT_CD_TRACK:
                return "Current CD track";
                
            case ID_AUDIO_MENU:
                return "Audio menu";
                
            case ID_PARKTRONIC:
                return "Parktronic";
        }
        
        return null;
    }

}
