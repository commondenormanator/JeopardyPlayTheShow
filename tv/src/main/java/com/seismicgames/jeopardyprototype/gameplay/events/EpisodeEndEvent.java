package com.seismicgames.jeopardyprototype.gameplay.events;

/**
 * Created by jduffy on 7/7/16.
 */
public class EpisodeEndEvent extends EpisodeEvent {
    public EpisodeEndEvent(int timestamp) {
        super(timestamp, Type.EpisodeEnd);
    }
}
