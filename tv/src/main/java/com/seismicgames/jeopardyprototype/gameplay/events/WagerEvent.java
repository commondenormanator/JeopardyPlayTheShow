package com.seismicgames.jeopardyprototype.gameplay.events;

import com.seismicgames.jeopardyprototype.episode.QuestionInfo;

/**
 * Created by jduffy on 7/7/16.
 */
public class WagerEvent extends EpisodeEvent {

    public WagerEvent(int timestamp) {
        super(timestamp, Type.Wager);
    }
}
