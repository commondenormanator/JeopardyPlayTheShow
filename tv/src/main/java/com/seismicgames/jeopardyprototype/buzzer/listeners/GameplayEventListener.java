package com.seismicgames.jeopardyprototype.buzzer.listeners;

import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.PauseGameRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.buzzer.message.WagerRequest;

public interface GameplayEventListener {

    void onUserBuzzIn();

    void onUserRestart();

    void onUserAnswer(AnswerRequest request);

    void onVoiceCaptureState(VoiceCaptureState request);

    void onUserWager(WagerRequest request);

    void onPauseGameRequest(PauseGameRequest request);

    void onQuitGameRequest();

    //demo methods
    void onMarkerRequest();
    void onUserJumpToMarker(int markerIndex);


}