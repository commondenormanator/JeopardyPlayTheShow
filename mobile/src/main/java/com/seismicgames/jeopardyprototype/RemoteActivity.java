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

    @OnClick(R.id.dpad_up)
    public void onUp(){
        mConnection.remoteSender().sendKeyEvent(KeyEvent.KEYCODE_DPAD_UP);

    }
    @OnClick(R.id.dpad_down)
    public void onDown(){
        mConnection.remoteSender().sendKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN);

    }
    @OnClick(R.id.dpad_left)
    public void onLeft(){
        mConnection.remoteSender().sendKeyEvent(KeyEvent.KEYCODE_DPAD_LEFT);

    }
    @OnClick(R.id.dpad_right)
    public void onRight(){
        mConnection.remoteSender().sendKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);

    }

    @Override
    public void onBackPressed() {
        if(mConnection.isBuzzerConnected()) {
            mConnection.remoteSender().sendKeyEvent(KeyEvent.KEYCODE_BACK);
        } else {
            super.onBackPressed();
        }
    }
}
