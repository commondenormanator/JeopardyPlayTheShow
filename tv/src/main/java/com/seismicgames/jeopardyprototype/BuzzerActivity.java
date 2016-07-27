package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;
import com.seismicgames.jeopardyprototype.buzzer.listeners.ConnectionEventListener;
import com.seismicgames.jeopardyprototype.buzzer.listeners.RemoteEventListener;
import com.seismicgames.jeopardyprototype.buzzer.message.RemoteKeyMessage;

/**
 * Created by jduffy on 7/19/16.
 */
public abstract class BuzzerActivity extends Activity {

    private static BuzzerScene.Scene scene;

    private boolean isVisible;
    protected BuzzerConnectionManager buzzerConnection;
    private BuzzerActivityListener mListener = new BuzzerActivityListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buzzerConnection = BuzzerConnectionManager.getInstance(getApplication());

    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        setScene(getDefaultScene());
        buzzerConnection.addListener((ConnectionEventListener) mListener);
        buzzerConnection.addListener((RemoteEventListener) mListener);
        if (buzzerConnection.isBuzzerConnected()) {
            sendSceneInfo();
        } else {
            onDisconnect();
        }
    }

    @Override
    protected void onPause() {
        isVisible = false;
        buzzerConnection.removeListener((ConnectionEventListener) mListener);
        buzzerConnection.removeListener((RemoteEventListener) mListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void onDisconnect() {
        BuzzerDisconnectedActivity.show(this);
    }

    private BuzzerScene.Scene getDefaultScene(){
        BuzzerScene annotation = getClass().getAnnotation(BuzzerScene.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

    public void setScene(BuzzerScene.Scene scene) {
        BuzzerActivity.scene = scene;
    }

    public void sendSceneInfo() {
        if (scene != null) {
            buzzerConnection.sceneSender().sendSceneInfo(scene.name());
        }
    }

    private class BuzzerActivityListener implements ConnectionEventListener, RemoteEventListener {
        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {

            if (isConnected) {
                sendSceneInfo();
            } else {
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
        @Override
        public void onKeyEvent(final RemoteKeyMessage message) {
//            Instrumentation m_Instrumentation = new Instrumentation();
//            m_Instrumentation.sendKeyDownUpSync( KeyEvent.KEYCODE_ENTER );


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BuzzerActivity.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    BuzzerActivity.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                }
            });
        }
    }
}