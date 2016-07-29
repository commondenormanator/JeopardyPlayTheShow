package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;
import com.seismicgames.jeopardyprototype.buzzer.client.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.ConnectionEventListener;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.SceneEventListener;
import com.seismicgames.jeopardyprototype.buzzer.message.SceneInfoMessage;

/**
 * Created by jduffy on 7/21/16.
 */
public class ConnectedActivity extends Activity implements SceneEventListener, ConnectionEventListener {

    protected BuzzerConnectionManager mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnection = BuzzerConnectionManager.getInstance(getApplication());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!mConnection.isBuzzerConnected()) {
            showDisconnectedActivity();
            return;
        }
        mConnection.addListener((ConnectionEventListener) this);
        mConnection.addListener((SceneEventListener) this);
        changeSceneIfNeeded(this, mConnection.getScene());
    }

    @Override
    protected void onPause() {
        mConnection.removeListener((ConnectionEventListener) this);
        mConnection.removeListener((SceneEventListener) this);
        super.onPause();
    }

    public static void changeSceneIfNeeded(Activity currActivity, SceneInfoMessage message) {
        BuzzerScene.Scene scene = message == null ? null : BuzzerScene.Scene.parse(message.scene);
        if (scene == null) scene = BuzzerScene.Scene.REMOTE;

        if (currActivity.getClass().getAnnotation(BuzzerScene.class) == null ||
                scene != currActivity.getClass().getAnnotation(BuzzerScene.class).value()) {

            currActivity.finish();

            startActivity(currActivity, scene);
        }
    }

    private static void startActivity(Activity context, BuzzerScene.Scene scene) {
        switch (scene) {

            case BUZZER:
                context.startActivity(new Intent(context, GameBuzzerActivity.class));
                break;
            case WAGER:
                context.startActivity(new Intent(context, WagerActivity.class));
                break;
            case REMOTE:
                context.startActivity(new Intent(context, RemoteActivity.class));
                break;
        }
    }

    @Override
    public void onSceneInfo(SceneInfoMessage message) {
        changeSceneIfNeeded(this, message);
    }

    private void showDisconnectedActivity(){
        finish();
        DisconnectedActivity.show(this);
    }

    @Override
    public void onBuzzerConnectivityChange(boolean isConnected) {
        if(!isConnected){
            showDisconnectedActivity();
        }
    }
}
