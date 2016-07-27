package com.seismicgames.jeopardyprototype.buzzer.client.senders;

import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;

import java.util.List;

/**
 * Created by jduffy on 7/21/16.
 */
public interface GameplayMessageSender {
    void sendBuzzInRequest();
    void sendAnswerRequest(List<String> answers);
    void sendRestartRequest();
    void sendVoiceState(VoiceCaptureState.State state);
    void sendWagerRequest(int wager);
}
