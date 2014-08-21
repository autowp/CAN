package com.autowp.canhacker.command;

@SuppressWarnings("serial")
public class CommandException extends Exception {
    public CommandException(String message)
    {
        super(message);
    }
}
