package com.seismicgames.jeopardyprototype.gameplay.events;

/**
 * Created by jduffy on 7/7/16.
 */
public class HomePlayerIntroEvent extends EpisodeEvent {
    public HomePlayerIntroEvent(int timestamp) {
        super(timestamp, Type.HomePlayerIntro);
    }
}
