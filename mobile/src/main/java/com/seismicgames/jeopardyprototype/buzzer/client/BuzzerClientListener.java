package com.seismicgames.jeopardyprototype.buzzer.client;

/**
 * Created by jduffy on 7/19/16.
 */
public interface BuzzerClientListener {
    void onConnectionClosed();
    void onMessage(String message);
}
