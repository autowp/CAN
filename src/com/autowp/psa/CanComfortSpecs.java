package com.autowp.psa;

import com.autowp.can.CanBusSpecs;

public class CanComfortSpecs extends CanBusSpecs {
    public CanComfortSpecs()
    {
        this.speed = CanComfort.SPEED;
        this.multiframeAbitrationID = new int[] {
            CanComfort.ID_TRACK_LIST, CanComfort.ID_CURRENT_CD_TRACK
        };
    }
}
