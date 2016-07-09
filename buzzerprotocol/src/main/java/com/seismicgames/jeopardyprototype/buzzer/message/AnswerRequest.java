package com.seismicgames.jeopardyprototype.buzzer.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jduffy on 6/28/16.
 */
public class AnswerRequest extends BuzzerMessage {
    public List<String> answers;

    public AnswerRequest(List<String> answers) {
        this.answers = answers;
    }
}
