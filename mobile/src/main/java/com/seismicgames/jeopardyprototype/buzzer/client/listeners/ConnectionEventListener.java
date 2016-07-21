package com.seismicgames.jeopardyprototype.buzzer.client.listeners;

public interface ConnectionEventListener {
    void onBuzzerConnectivityChange(boolean isConnected);
}