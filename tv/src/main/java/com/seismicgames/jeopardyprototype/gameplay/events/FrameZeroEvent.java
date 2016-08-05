package com.seismicgames.jeopardyprototype.gameplay.events;

import com.seismicgames.jeopardyprototype.util.TimeCode;

/**
 * Created by jduffy on 7/18/16.
 */
public class FrameZeroEvent extends EpisodeEvent {

    private static final int HourOne = 60*60*1000;

    public FrameZeroEvent(String time) {
        super(-1 * TimeCode.parse(time), Type.FrameZero);
    }



}
