package com.seismicgames.jeopardyprototype.buzzer.sender;

import com.seismicgames.jeopardyprototype.episode.EpisodeMarker;

import java.util.List;

/**
 * Created by jduffy on 7/20/16.
 */
public interface GameplayMessageSender {
    void sendBuzzInResponse(boolean isValidBuzz);
    void sendStopVoiceRec();

    //demo methods
    void sendEpisodeMarkers(List<EpisodeMarker> markers);
}
