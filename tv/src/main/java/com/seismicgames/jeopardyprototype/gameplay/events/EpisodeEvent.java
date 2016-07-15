package com.seismicgames.jeopardyprototype.gameplay.events;

import com.seismicgames.jeopardyprototype.Constants;

/**
 * Created by jduffy on 6/29/16.
 */
public class EpisodeEvent {
    public enum Type{
        Skipped, EpisodeStart, Categories, QuestAsked, AnswerRead
    }
    public int timestamp;
    public Type type;

    public EpisodeEvent(int timestamp, Type type) {
        this.timestamp = timestamp + Constants.EventTimestampOffset;
        this.type = type;
    }
}
