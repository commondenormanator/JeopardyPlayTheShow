package com.seismicgames.jeopardyprototype.buzzer.listeners;

import android.view.KeyEvent;

import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;

public interface RemoteEventListener {
    void onKeyEvent(int keyCode);
}