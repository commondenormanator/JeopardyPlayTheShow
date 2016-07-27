package com.seismicgames.jeopardyprototype.gameplay.events;

import com.seismicgames.jeopardyprototype.episode.QuestionInfo;

/**
 * Created by jduffy on 7/7/16.
 */
public class QuestionAskedEvent extends EpisodeEvent {

    public QuestionInfo questionInfo;

    public QuestionAskedEvent(int timestamp, QuestionInfo q) {
        super(timestamp, Type.QuestAsked);
        this.questionInfo = q;
    }
}
