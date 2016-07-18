package com.seismicgames.jeopardyprototype.buzzer.message;

/**
 * Created by jduffy on 7/7/16.
 */
public class VoiceCaptureState extends BuzzerMessage {
    public enum State {
        LISTENING, SPEECH_BEGIN, SPEECH_END, RECOGNITION_COMPLETE
    }

    public State state;

    public VoiceCaptureState(State state) {
        this.state = state;
    }

}
