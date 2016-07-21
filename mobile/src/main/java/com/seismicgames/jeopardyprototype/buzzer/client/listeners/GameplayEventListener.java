package com.seismicgames.jeopardyprototype.buzzer.client.listeners;

import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;

public interface GameplayEventListener {

    void onBuzzInResponse(BuzzInResponse response);
}