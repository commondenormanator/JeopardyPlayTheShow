package com.seismicgames.jeopardyprototype.episode;

/**
 * Created by jduffy on 7/7/16.
 */
public class QuestionInfo {
    public final String category;
    public final String clue;
    public final String[] answers;
    public final int value;
    public final int readTimestamp;
    public final int answerTimestamp;

    public QuestionInfo(String category, String clue, String answerString, int value, String read_timestamp, String answer_timestamp) {
        this.category = category;
        this.clue = clue;
        this.answers = answerString.split("\\|");
        this.value = value;
        this.readTimestamp = parseTimestamp(read_timestamp);
        this.answerTimestamp = parseTimestamp(answer_timestamp);
    }


    private int parseTimestamp(String ts){
        int millis = 116000;
        millis += Integer.parseInt(ts.substring(3, 5)) * 60 *  1000;
        millis += Integer.parseInt(ts.substring(6, 8)) *  1000;
        millis += Integer.parseInt(ts.substring(9, 11)) * 1000d/ 29.97d;
        return millis;
    }
}
