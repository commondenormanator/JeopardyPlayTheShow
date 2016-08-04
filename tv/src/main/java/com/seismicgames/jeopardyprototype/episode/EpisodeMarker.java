package com.seismicgames.jeopardyprototype.episode;

import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeEvent;

/**
 * Created by jduffy on 8/3/16.
 */
public class EpisodeMarker {
    public final String name;
    public final int timestamp;

    public EpisodeMarker(String name, int timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }
    public EpisodeMarker(String name, EpisodeEvent event) {
        this.name = name;
        this.timestamp = event.timestamp - 100;
    }
}
