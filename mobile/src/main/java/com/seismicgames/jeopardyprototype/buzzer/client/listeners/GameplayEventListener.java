package com.seismicgames.jeopardyprototype.buzzer.client.listeners;

import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.EpisodeMarkerList;

public interface GameplayEventListener {

    void onBuzzInResponse(BuzzInResponse response);
    void onStopVoiceRec();
    void onEpisodeMarkers(EpisodeMarkerList markers);
}