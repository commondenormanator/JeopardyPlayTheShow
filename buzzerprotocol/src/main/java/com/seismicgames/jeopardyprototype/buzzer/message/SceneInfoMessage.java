package com.seismicgames.jeopardyprototype.buzzer.message;

/**
 * Created by jduffy on 6/28/16.
 */
public class SceneInfoMessage extends BuzzerMessage {

    public String scene;

    public SceneInfoMessage(String scene) {
        this.scene = scene;
    }
}
