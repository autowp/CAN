package com.autowp.canhacker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.autowp.can.CanAdapter;
import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.canhacker.command.Command;
import com.autowp.canhacker.command.CommandException;
import com.autowp.canhacker.command.TransmitCommand;
import com.autowp.canhacker.response.FrameResponse;
import com.autowp.canhacker.response.Response;

public abstract class CanHacker extends CanAdapter {
    
    public static final int VID = 0x0403;
    public static final int PID = 0x6001;
    
    /**
     * Gxx
     * Read register content from SJA1000 controller.
     * xx = Register to read (00-7F)
     * Return: Gdd[CR] 
     */
    public static final String G = "G";
    
    /**
     * Mxxxxxxxx[CR]
     * 
     * Set acceptance code register of SJA1000. This command works only if controller is setup with command “S” and in reset mode.
     * 
     * xxxxxxxx = Acceptance Code in hexadecimal, order ACR0 ACR1 ACR2 ACR3
     * Default value after power-up is 0x00000000 to receive all frames.
     * 
     * Return: [CR] or [BEL] 
     */
    public static final String M = "M";
    
    /**
     * mxxxxxxxx[CR]
     * 
     * Set acceptance mask register of SJA1000. This command works only if 
     * controller is setup with command “S” and in reset mode.
     * 
     * xxxxxxxx = Acceptance Mask in hexadecimal, order AMR0 AMR1 AMR2 AMR3
     * 
     * Default value after power-up is 0xFFFFFFFF to receive all frames.
     * 
     * Return [CR] or [BEL]
     * 
     * The acceptance filter is defined by the Acceptance Code Registers ACRn 
     * and the Acceptance Mask Registers AMRn. The bit patterns of messages 
     * to be received are defined within the acceptance code registers. 
     * The corresponding acceptance mask registers allow to define certain 
     * bit positions to be ‘don’t care’.
     * 
     * This device uses dual filter configuration.
     * For details of ACR and AMR usage see the SJA1000 datasheet. 
     */
    public static final String m = "m";
    
    /**
     * riiiL [CR]
     * 
     * This command transmits a standard remote 11 Bit CAN frame. 
     * It works only if controller is in operational mode after command “O”.
     * 
     * iii - Identifier in hexadecimal (000-7FF)
     * L   - Data length code (0-8)
     * 
     * Return: [CR] or [BEL] 
     */
    public static final String r = "r";
    
    /**
     * RiiiiiiiiL [CR]
     * 
     * This command transmits an extended remote 29 Bit CAN frame.
     * It works only if controller is in operational mode after command “O”.
     * 
     * iiiiiiii - Identifier in hexadecimal (00000000-1FFFFFFF) 
     * L        - Data length code (0-8)
     * 
     * Return: [CR] or [BEL] 
     */
    public static final String R = "R";

    /**
     * sxxyy[CR]
     * 
     * This command will set user defined values for the SJA1000 bit rate register BTR0 and BTR1.
     * It works only after power up or if controller is in reset mode after command “C”.
     * 
     * xx = hexadecimal value for BTR0 (00-FF)
     * yy = hexadecimal value for BTR1 (00-FF)
     * 
     * Return: [CR] or [BEL] 
     */
    public static final String s = "s";
    
    /**
     * TiiiiiiiiLDDDDDDDDDDDDDDDD[CR]
     * 
     * This command transmits an extended 29 Bit CAN frame. 
     * It works only if controller is in operational mode after command “O”.
     * 
     * iiiiiiii = Identifier in hexadecimal (00000000-1FFFFFFF)
     * L        = Data length code (0-8)
     * DD       = Data byte value in hexadecimal (00-FF). Number of given data bytes will be checked against given data length code.
     * 
     * Return: [CR] or [BEL] 
     */
    public static final String T = "T";
    
    /**
     * Wrrdd[CR]
     * 
     * Write SJA1000 register with data.
     * The data will be written to specified register without any check!
     * 
     * rr = Register number (00-7F)
     * dd = Data byte (00-FF)
     * 
     * Return: [CR] 
     */
    public static final String W = "W";
    
    public static final char COMMAND_DELIMITER = '\r';
    
    protected static final char BELL = (char)0x07;
    
    public interface OnCommandSentListener {
        public void handleCommandSentEvent(Command command);
    }
    
    public interface OnResponseReceivedListener {
        public void handleResponseReceivedEvent(Response response);
    }
    
    private List<OnCommandSentListener> mCommandSendListeners = new ArrayList<OnCommandSentListener>();
    
    private List<OnResponseReceivedListener> mResponseReceivedListeners = new ArrayList<OnResponseReceivedListener>();
    
    public synchronized void addEventListener(OnCommandSentListener listener)
    {
        mCommandSendListeners.add(listener);
    }
    
    public synchronized void removeEventListener(OnCommandSentListener listener)
    {
        mCommandSendListeners.remove(listener);
    }
    
    protected synchronized void fireCommandSendEvent(Command command) throws CanFrameException
    {
        Iterator<OnCommandSentListener> i = mCommandSendListeners.iterator();
        while(i.hasNext())  {
            ((OnCommandSentListener) i.next()).handleCommandSentEvent(command);
        }
        
        if (command instanceof TransmitCommand) {
            TransmitCommand transmitCommand = (TransmitCommand)command;
            
            CanFrame frame = new CanFrame(transmitCommand.getId(), transmitCommand.getData());
            
            fireFrameSentEvent(frame);
        }
    }
    
    public synchronized void addEventListener(OnResponseReceivedListener listener) 
    {
        mResponseReceivedListeners.add(listener);
    }
    
    public synchronized void removeEventListener(OnResponseReceivedListener listener)   
    {
        mResponseReceivedListeners.remove(listener);
    }
    
    protected synchronized void fireResponseReceivedEvent(Response response) throws CanFrameException 
    {
        Iterator<OnResponseReceivedListener> i = mResponseReceivedListeners.iterator();
        while(i.hasNext())  {
            ((OnResponseReceivedListener) i.next()).handleResponseReceivedEvent(response);
        }
        
        if (response instanceof FrameResponse) {
            FrameResponse frameResponse = (FrameResponse)response;
            
            CanFrame frame = new CanFrame(frameResponse.getId(), frameResponse.getData());
            
            this.fireFrameReceivedEvent(frame);
        }
    }
    
    @Override
    public void send(CanFrame message) throws CanHackerException
    {
        try {
            TransmitCommand command = new TransmitCommand(message.getId(), message.getData());
            this.send(command);
        } catch (CommandException e) {
            throw new CanHackerException(e.getMessage());
        }
    }
    
    public abstract CanHacker send(Command c) throws CanHackerException;
}
