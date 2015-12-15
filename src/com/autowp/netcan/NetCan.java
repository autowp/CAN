package com.autowp.netcan;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.codec.binary.Hex;

import com.autowp.can.CanAdapterException;
import com.autowp.can.CanFrameException;
import com.autowp.canhacker.CanHacker;
import com.autowp.canhacker.command.Command;
import com.autowp.canhacker.response.Response;
import com.autowp.canhacker.response.ResponseException;

public class NetCan extends CanHacker {
    private String mHostname = "127.0.0.1";
    private int mPort = 20100;
    private Socket mSocket = null;

    public NetCan setPort(int port) {
        mPort = port;
        
        return this;
    }
    
    public NetCan setHostname(String hostname) {
        mHostname = hostname;
        
        return this;
    }

    @Override
    public void connect() throws CanAdapterException {
        try {
            mSocket = new Socket(mHostname, mPort);
            
            readerThread.start();
            
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            readerThread.interrupt();
            mSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mSocket = null;
    }

    @Override
    public boolean isConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    public synchronized NetCan send(Command c) throws NetCanException
    {
        if (!this.isConnected()) {
            throw new NetCanException("NetCan is not connected");
        }
        
        String command = c.toString() + COMMAND_DELIMITER;
        
        try {
            
            OutputStream out = mSocket.getOutputStream();

            out.write(command.getBytes("ISO-8859-1"));
            out.flush();
            
        } catch (IOException e) {
            throw new NetCanException("I/O error: " + e.getMessage());
        }
                
        return this;
    }
    
    private Thread readerThread = new Thread() {
        private byte[] buffer = new byte[1024];
        private int bufferPos = 0;
        
        public void run() {
            InputStream is;
            try {
                is = mSocket.getInputStream();
                byte[] readBuffer = new byte[2000];
    
                while(true) {
                    int bytesRead = is.read(readBuffer);
                    
                    for (int i=0; i<bytesRead; i++) {
                        char dataChar = (char)readBuffer[i];
                        if (dataChar != COMMAND_DELIMITER) {
                            buffer[bufferPos++] = (byte)readBuffer[i];
                        }
                        
                        if (dataChar == COMMAND_DELIMITER || dataChar == BELL) {
                            if (bufferPos > 0) {
                                byte[] commandBytes = new byte[bufferPos];
                                System.arraycopy(buffer, 0, commandBytes, 0, bufferPos);
                                System.out.println("Found command: " + new String(commandBytes));
                                Response response = Response.fromBytes(commandBytes);
                                fireResponseReceivedEvent(response);
                            }
                            bufferPos = 0;
                        }
                        
                        byte[] commandBytes = new byte[bufferPos];
                        System.arraycopy(buffer, 0, commandBytes, 0, bufferPos);
                    }
                    
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ResponseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (CanFrameException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
      
}
