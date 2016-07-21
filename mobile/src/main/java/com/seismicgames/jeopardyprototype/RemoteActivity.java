package com.seismicgames.jeopardyprototype;

import android.os.Bundle;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;

@BuzzerScene(BuzzerScene.Scene.REMOTE)
public class RemoteActivity extends ConnectedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
    }
}
