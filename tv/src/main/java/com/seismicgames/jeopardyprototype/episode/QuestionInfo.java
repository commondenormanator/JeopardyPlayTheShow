package com.seismicgames.jeopardyprototype.episode;

import com.seismicgames.jeopardyprototype.gameplay.score.AnswerUtil;
import com.seismicgames.jeopardyprototype.util.TimeCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jduffy on 7/7/16.
 */
public class QuestionInfo {
    public enum QuestionType{
        S,DD,FJ
    }

    public final QuestionType type;
    public final String category;
    public final String clue;
    public final String[] answers;
    public final int value;
    public final int readTimestamp;
    public final int answerTimestamp;

    public QuestionInfo(QuestionType type, String category, String clue, String answerString, int value, String read_timestamp, String answer_timestamp) {
        this.type = type;
        this.category = category;
        this.clue = clue;
        this.answers = parseAnswer(answerString);
        this.value = value;
        this.readTimestamp = TimeCode.parse(read_timestamp);
        this.answerTimestamp = TimeCode.parse(answer_timestamp);
    }


    private static String[] parseAnswer(String answerString) {
        String[] splitAnswer = answerString.toLowerCase().split("\\|");

//        return AnswerUtil.getAnswerPermutations(splitAnswer);
        return splitAnswer;

    }


}
