package com.seismicgames.jeopardyprototype.buzzer.client.senders;

import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;

import java.util.List;

/**
 * Created by jduffy on 7/21/16.
 */
public interface RemoteMessageSender {
    void sendKeyEvent(int keyCode);
}
