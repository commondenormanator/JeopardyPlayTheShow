package com.seismicgames.jeopardyprototype.buzzer.client;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.ConnectionEventListener;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.GameplayEventListener;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.SceneEventListener;
import com.seismicgames.jeopardyprototype.buzzer.client.senders.GameplayMessageSender;
import com.seismicgames.jeopardyprototype.buzzer.client.senders.RemoteMessageSender;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.RemoteKeyMessage;
import com.seismicgames.jeopardyprototype.buzzer.message.RestartRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.SceneInfoMessage;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.buzzer.message.WagerRequest;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jduffy on 7/19/16.
 */
public class BuzzerConnectionManager {
    private static final String TAG = BuzzerConnectionManager.class.getName();

    private static BuzzerConnectionManager mInstance;

    public static synchronized BuzzerConnectionManager getInstance(Application app) {
        if (mInstance == null) mInstance = new BuzzerConnectionManager(app);
        return mInstance;
    }

    private int foregroundActivityCount = 0;
    Application.ActivityLifecycleCallbacks lifecycleCallbacks = new LifecycleCallbacks();

    private final InternalListener mListener = new InternalListener();

    private HostScanner mHostScanner = new HostScanner();
    private HostScannerListener mScannerListener = new HostScannerListener();
    private BuzzerClient mClient;

    private final Gson gson = new Gson();

    private SceneInfoMessage scene = null;

    private final BuzzerClientListener mClientListener = new BuzzerClientListener() {
        @Override
        public void onConnectionClosed() {
            mListener.onBuzzerConnectivityChange(false);
            if(foregroundActivityCount > 0) mHostScanner.scanForHost(mScannerListener);
        }

        @Override
        public void onMessage(String message) {
            JsonObject json = gson.fromJson(message, JsonObject.class);

            switch ( json.get("type").getAsString()){
                case "BuzzInResponse":
                    mListener.onBuzzInResponse(gson.fromJson(json, BuzzInResponse.class));
                    break;
                case "SceneInfoMessage":
                    mListener.onSceneInfo(gson.fromJson(json, SceneInfoMessage.class));
                    break;
                case "StopVoiceRecRequest":
                    mListener.onStopVoiceRec();
                    break;
                default:
                    Log.e(TAG, "Unhandled message: " + message);
            }
        }
    };



    private BuzzerConnectionManager(Application app) {
        app.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private final InternalSender mSender = new InternalSender();

    public GameplayMessageSender gameplaySender(){
        return mSender;
    }
    public RemoteMessageSender remoteSender(){
        return mSender;
    }

    public void addListener(ConnectionEventListener listener){
        mListener.connListeners.add(listener);
    }
    public void removeListener(ConnectionEventListener listener){
        mListener.connListeners.remove(listener);
    }

    public void addListener(GameplayEventListener listener){
        mListener.gameListeners.add(listener);
    }
    public void removeListener(GameplayEventListener listener){
        mListener.gameListeners.remove(listener);
    }

    public void addListener(SceneEventListener listener){
        mListener.sceneListeners.add(listener);
    }
    public void removeListener(SceneEventListener listener){
        mListener.sceneListeners.remove(listener);
    }

    public boolean isBuzzerConnected() {
        return mClient != null && mClient.getConnection().isOpen();
    }

    public SceneInfoMessage getScene(){
        return scene;
    }

    private class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivityCount++;
            if (foregroundActivityCount == 1 && mClient == null && !mHostScanner.isScanning()) {
                Log.d(TAG, "start scan");
                mHostScanner.scanForHost(mScannerListener);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            foregroundActivityCount--;
            if (foregroundActivityCount == 0) {
                Log.d(TAG, "stop server");

                mHostScanner.stop();
                if(mClient != null) {
                    mClient.setConnectionListener(null);
                    mClient.close();
                    mClient = null;
                }

            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    private class HostScannerListener implements HostScanner.Listener{

        @Override
        public void onHostFound(BuzzerClient client) {
            mClient = client;
            mClient.setConnectionListener(mClientListener);
            mListener.onBuzzerConnectivityChange(true);
        }
    }


    private class InternalListener implements ConnectionEventListener, GameplayEventListener, SceneEventListener {

        public final Set<ConnectionEventListener> connListeners = Collections.synchronizedSet(new HashSet<ConnectionEventListener>());
        public final Set<GameplayEventListener> gameListeners = Collections.synchronizedSet(new HashSet<GameplayEventListener>());
        public final Set<SceneEventListener> sceneListeners = Collections.synchronizedSet(new HashSet<SceneEventListener>());

        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {
            synchronized (connListeners) {
                for (ConnectionEventListener l : connListeners) {
                    l.onBuzzerConnectivityChange(isConnected);
                }
            }
        }


        @Override
        public void onBuzzInResponse(BuzzInResponse response) {
            synchronized (gameListeners) {
                for (GameplayEventListener l : gameListeners) {
                    l.onBuzzInResponse(response);
                }
            }
        }

        @Override
        public void onStopVoiceRec() {
            synchronized (gameListeners) {
                for (GameplayEventListener l : gameListeners) {
                    l.onStopVoiceRec();
                }
            }
        }

        @Override
        public void onSceneInfo(SceneInfoMessage message) {
            scene = message;
            synchronized (sceneListeners) {
                for (SceneEventListener l : sceneListeners) {
                    l.onSceneInfo(message);
                }
            }
        }
    }


    private class InternalSender implements GameplayMessageSender, RemoteMessageSender {

        private void send(String message) {
            if (mClient != null) mClient.send(message);
        }

        public void sendBuzzInRequest() {
            send(gson.toJson(new BuzzInRequest()));
        }

        public void sendAnswerRequest(List<String> answers) {
            send(gson.toJson(new AnswerRequest(answers)));
        }

        public void sendRestartRequest() {
            send(gson.toJson(new RestartRequest()));
        }

        public void sendVoiceState(VoiceCaptureState.State state) {
            send(gson.toJson(new VoiceCaptureState(state)));
        }

        @Override
        public void sendKeyEvent(int keyCode) {
            send(gson.toJson(new RemoteKeyMessage(keyCode)));
        }

        public void sendWagerRequest(int wager) {
            send(gson.toJson(new WagerRequest(wager)));
        }
    }

}
