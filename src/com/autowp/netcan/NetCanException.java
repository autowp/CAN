package com.autowp.netcan;

import com.autowp.canhacker.CanHackerException;

@SuppressWarnings("serial")
public class NetCanException extends CanHackerException {
    public NetCanException(String message)
    {
        super(message);
    }

}
