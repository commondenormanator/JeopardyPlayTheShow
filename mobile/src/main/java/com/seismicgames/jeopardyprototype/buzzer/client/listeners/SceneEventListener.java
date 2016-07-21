package com.seismicgames.jeopardyprototype.buzzer.client.listeners;

import com.seismicgames.jeopardyprototype.buzzer.message.SceneInfoMessage;

public interface SceneEventListener {
    void onSceneInfo(SceneInfoMessage request);
}