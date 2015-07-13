package com.autowp.psa;

import java.nio.charset.Charset;

import com.autowp.can.CanMessage;

public class CanComfort {
    public static final int SPEED = 125; // kbit
    
    public static final int ID_BSI_STATUS = 0x036;
    public static final int ID_CURRENT_CD_TRACK = 0x0A4;
    public static final int ID_DISPLAY_UNKNOWN1 = 0x0DF;
    public static final int ID_PARKTRONIC = 0x0E1;
    public static final int ID_BSI_INFO = 0x0F6;
    public static final int ID_TRACK_LIST = 0x125;
    public static final int ID_DISPLAY_STATUS = 0x167;
    public static final int ID_BSI_INFO_WINDOW = 0x1A1;
    public static final int ID_VOLUME = 0x1A5;
    public static final int ID_RADIO_1 = 0x1E0;
    public static final int ID_DISPLAY_CONDITIONING = 0x1ED;
    public static final int ID_AUDIO_MENU = 0x1E5;
    public static final int ID_COLUMN_KEYPAD = 0x21F;
    public static final int ID_RDS = 0x265;
    public static final int ID_VIN = 0x2B6;
    public static final int ID_CURRENT_CD_TRACK_INFO = 0x3A5;
    public static final int ID_RADIO_KEYPAD = 0x3E5;
    public static final int ID_TIME = 0x3F6;
    
    public static final int BSI_STATUS_PERIOD = 100;
    public static final int BSI_INFO_PERIOD = 500;
    public static final int BSI_INFO_WINDOW_PERIOD = 200;
    
    public static final int VIN_DELAY = 300;
    public static final int VIN_LENGTH = 8;
    
    public static final int TRACK_LIST_TRACK_AUTHOR_LENGTH = 20;
    public static final int TRACK_LIST_TRACK_NAME_LENGTH = 20;
    
    public static final int VOLUME_MIN = 0;
    public static final int VOLUME_MAX = 30;
    
    public static final Charset charset = Charset.forName("ISO-8859-1");
    
    public static String getMessageComment(CanMessage message)
    {
        switch (message.getId()) {
            case ID_BSI_STATUS:
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
