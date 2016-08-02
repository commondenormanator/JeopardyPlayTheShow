package com.seismicgames.jeopardyprototype.gameplay.events;

import com.seismicgames.jeopardyprototype.episode.QuestionInfo;

/**
 * Created by jduffy on 7/7/16.
 */
public class WagerEvent extends EpisodeEvent {

    public final QuestionInfo questionInfo;

    public WagerEvent(int timestamp, QuestionInfo questionInfo) {
        super(timestamp, Type.Wager);
        this.questionInfo = questionInfo;
    }
}
