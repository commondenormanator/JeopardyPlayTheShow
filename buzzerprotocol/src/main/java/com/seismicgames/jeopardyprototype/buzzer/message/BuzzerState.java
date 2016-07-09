package com.seismicgames.jeopardyprototype.buzzer.message;

/**
 * Created by jduffy on 7/7/16.
 */
public class BuzzerState extends BuzzerMessage {
    public enum State {
        CAN_BUZZ_IN, WAIT_FOR_QUESTION, CAN_SUBMIT_ANSWER
    }
    public State state;
}
