package com.seismicgames.jeopardyprototype.buzzer.message;

import java.util.List;

/**
 * Created by jduffy on 6/28/16.
 */
public class EpisodeMarkerList extends BuzzerMessage {
    public List<String> markers;

    public EpisodeMarkerList(List<String> markers) {
        this.markers = markers;
    }
}
