package com.autowp.arduinocan;

import com.autowp.can.CanAdapterException;

public class ArduinoCanException extends CanAdapterException {

    private static final long serialVersionUID = 1L;

    public ArduinoCanException(String message) {
        super(message);
    }

}
