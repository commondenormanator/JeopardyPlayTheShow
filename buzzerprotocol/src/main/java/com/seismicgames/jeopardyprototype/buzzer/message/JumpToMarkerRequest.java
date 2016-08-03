package com.seismicgames.jeopardyprototype.buzzer.message;

import java.util.List;

/**
 * Created by jduffy on 6/28/16.
 */
public class JumpToMarkerRequest extends BuzzerMessage {
    public int markerIndex;

    public JumpToMarkerRequest(int markerIndex) {
        this.markerIndex = markerIndex;
    }
}
