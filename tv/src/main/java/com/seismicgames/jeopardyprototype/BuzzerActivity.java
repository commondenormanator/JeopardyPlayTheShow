package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.os.Bundle;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.listeners.ConnectionEventListener;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;

/**
 * Created by jduffy on 7/19/16.
 */
public abstract class BuzzerActivity extends Activity {

    private static BuzzerScene.Scene scene;

    private boolean isVisible;
    protected BuzzerConnectionManager buzzerConnection;
    private ConnectionEventListener mListener = new ConnectionEventListener() {
        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {

            if(isConnected) {
                sendSceneInfo();
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isVisible) {
                            onDisconnect();
                        }
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buzzerConnection = BuzzerConnectionManager.getInstance(getApplication());

    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        setScene();
        buzzerConnection.addListener(mListener);
        if(buzzerConnection.isBuzzerConnected()) {
            sendSceneInfo();
        } else {
            onDisconnect();
        }
    }

    @Override
    protected void onPause() {
        isVisible = false;
        buzzerConnection.removeListener(mListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void onDisconnect(){
        BuzzerDisconnectedActivity.show(this);
    }


    private void setScene(){
        BuzzerScene annotation = getClass().getAnnotation(BuzzerScene.class);
        if(annotation != null){
            scene = annotation.value();
        }
    }

    private void sendSceneInfo(){
        if(scene != null) {
            buzzerConnection.sceneSender().sendSceneInfo(scene.name());
        }
    }
}