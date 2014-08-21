package com.autowp.peugeot.parktronic;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.autowp.can.CanClient;
import com.autowp.can.CanClientException;
import com.autowp.can.CanFrame;
import com.autowp.can.CanFrameException;
import com.autowp.peugeot.CanComfort;

public class ParktronicEmulation {
    private CanClient mCanClient;
    
    private boolean mEmulateReceive = false;
    
    private static List<String> sCommands = Arrays.asList(
        "B0003FFCFCBE00",
        "B0003FFCFCBE00",
        "B0003FFCFCBE00",
        "B0F017FCFC9E00",
        "B0F017FCFC9E00",
        "B0F017FCFC9E00",
        "B0F017FCFC9E00",
        "B0F016FCFC7E00",
        "B0F016FCFC7E00",
        "B0F016FCFC7E00",
        "B0F015FCFC7E00",
        "B0F015FCFC7E00",
        "B0F015FCFC7E00",
        "B0F015FCFC7E00",
        "B0F014FCFC7E00",
        "B0F014FCFC7E00",
        "B0F014FCFC7E00",
        "B0F012FCFC5E00",
        "B0F012FCFC5E00",
        "B0F012FCFC5E00",
        "B0F011FCFC5E00",
        "B0F011FCFC5E00",
        "B0F011FCFC5E00",
        "B0F010FCFC5E00",
        "B0F010FCFC5E00",
        "B0F010FCFC4A00",
        "B0F010FCE84A00",
        "B0F00FFCE82A00",
        "B0F00FFCE82A00",
        "B0F00FFCE82A00",
        "B0F00EFCE82A00",
        "B0F00EFCE82A00",
        "B0F00EFCE82A00",
        "B0F00EFCE82A00",
        "B0F00EFCE82A00",
        "B0F00EFCE82A00",
        "B0F00EFCE82A00",
        "B0F00CFCE82A00",
        "B0F00CFCE82A00",
        "B0F00CFCE82600",
        "B0F00CFCE42600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "B0F000FCE40600",
        "90F000FCE40600",
        "90F000FCE40600",
        "90F000FCE40600",
        "90F000FCE40600", 
        "90F000FCE40600"
    );
    
    private class ScheduledTask extends TimerTask {
        
        private int index = 0;
        
        // Add your task here
        public void run() {
            
            String command = sCommands.get(index++ % sCommands.size());
            
            try {
                byte[] data = Hex.decodeHex(command.toCharArray());
                CanFrame frame = new CanFrame(CanComfort.ID_PARKTRONIC, data);
                if (mEmulateReceive) {
                    mCanClient.receive(frame);
                } else {
                    mCanClient.send(frame);
                }
                
            } catch (CanClientException e) {
                this.cancel();
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (CanFrameException e) {
                this.cancel();
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (DecoderException e) {
                this.cancel();
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private ScheduledTask mScheduledTask;
    
    public ParktronicEmulation(CanClient canClient, boolean emulateReceive)
    {
        mCanClient = canClient;
        mEmulateReceive = emulateReceive;
    }

    public void start()
    {
        stop();
        
        Timer time = new Timer();
        mScheduledTask = new ScheduledTask();
        time.schedule(mScheduledTask, 0, 250); 
    }
    
    public void stop()
    {
        if (mScheduledTask != null) {
            mScheduledTask.cancel();
            mScheduledTask = null;
        }
    }
}
