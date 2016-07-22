package com.seismicgames.jeopardyprototype.buzzer.message;

/**
 * Created by jduffy on 6/28/16.
 */
public class RemoteKeyMessage extends BuzzerMessage {

    public int key;

    public RemoteKeyMessage(int key) {
        this.key = key;
    }
}
