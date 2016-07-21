package com.seismicgames.jeopardyprototype.buzzer;

import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;

public interface BuzzerServerListener {
    void onBuzzerConnectivityChange(boolean isConnected);

    void onMessage(String s);


}