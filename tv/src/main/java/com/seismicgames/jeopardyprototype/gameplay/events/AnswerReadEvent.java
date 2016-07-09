package com.seismicgames.jeopardyprototype.gameplay.events;

import com.seismicgames.jeopardyprototype.episode.QuestionInfo;

/**
 * Created by jduffy on 7/7/16.
 */
public class AnswerReadEvent extends EpisodeEvent {

    public QuestionInfo questionInfo;

    public AnswerReadEvent(int timestamp, QuestionInfo q) {
        super(timestamp, Type.AnswerRead);
        this.questionInfo = q;
    }
}
