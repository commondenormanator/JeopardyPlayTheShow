package com.seismicgames.jeopardyprototype.buzzer.listeners;

import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;

public interface ConnectionEventListener {
    void onBuzzerConnectivityChange(boolean isConnected);
}