package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;
import com.seismicgames.jeopardyprototype.buzzer.client.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.SceneEventListener;
import com.seismicgames.jeopardyprototype.buzzer.message.SceneInfoMessage;

/**
 * Created by jduffy on 7/21/16.
 */
public class ConnectedActivity extends Activity implements SceneEventListener{

    protected BuzzerConnectionManager mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnection = BuzzerConnectionManager.getInstance(getApplication());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mConnection.addListener(this);
        changeSceneIfNeeded(mConnection.getScene());
    }

    @Override
    protected void onPause() {
        mConnection.removeListener(this);
        super.onPause();
    }


    private void changeSceneIfNeeded(SceneInfoMessage message){
        //scene unknown
        if(message == null) return;

        BuzzerScene.Scene scene  = BuzzerScene.Scene.parse(message.scene);
        if(scene == null) return;

        if(scene != getClass().getAnnotation(BuzzerScene.class).value()){

            finish();
            startActivity(scene);
        }
    }

    private void startActivity(BuzzerScene.Scene scene){
        switch (scene){

            case REMOTE:
                startActivity(new Intent(this, RemoteActivity.class));
                break;
            case BUZZER:
                startActivity(new Intent(this, GameBuzzerActivity.class));
                break;
        }
    }

    @Override
    public void onSceneInfo(SceneInfoMessage message) {
        changeSceneIfNeeded(message);
    }
}
