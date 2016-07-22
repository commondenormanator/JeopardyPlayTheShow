package com.seismicgames.jeopardyprototype;

import android.os.Bundle;
import android.view.KeyEvent;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;

import butterknife.ButterKnife;
import butterknife.OnClick;

@BuzzerScene(BuzzerScene.Scene.REMOTE)
public class RemoteActivity extends ConnectedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.okButton)
    public void onOk(){
        mConnection.remoteSender().sendKeyEvent(KeyEvent.KEYCODE_DPAD_CENTER);

    }
}
