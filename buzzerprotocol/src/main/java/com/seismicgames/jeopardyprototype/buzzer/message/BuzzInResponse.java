package com.seismicgames.jeopardyprototype.buzzer.message;

/**
 * Created by jduffy on 6/28/16.
 */
public class BuzzInResponse extends BuzzerMessage {

    public boolean buzzInValid;

    public BuzzInResponse(boolean buzzInValid) {
        this.buzzInValid = buzzInValid;
    }
}
