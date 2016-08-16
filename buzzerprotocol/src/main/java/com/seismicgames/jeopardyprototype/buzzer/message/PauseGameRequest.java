package com.seismicgames.jeopardyprototype.buzzer.message;

/**
 * Created by jduffy on 6/28/16.
 */
public class PauseGameRequest extends BuzzerMessage {
    public final boolean shouldPause;

    public PauseGameRequest(boolean shouldPause) {
        this.shouldPause = shouldPause;
    }
}
