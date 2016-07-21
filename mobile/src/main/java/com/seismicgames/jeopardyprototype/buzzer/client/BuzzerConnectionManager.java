package com.seismicgames.jeopardyprototype.buzzer.client;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.ConnectionEventListener;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.GameplayEventListener;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.SceneEventListener;
import com.seismicgames.jeopardyprototype.buzzer.client.senders.GameplayMessageSender;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.RestartRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.SceneInfoMessage;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jduffy on 7/19/16.
 */
public class BuzzerConnectionManager {
    private static final String TAG = BuzzerConnectionManager.class.getName();

    private static BuzzerConnectionManager mInstance;

    public static BuzzerConnectionManager getInstance(Application app) {
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
                default:
                    Log.e(TAG, "Unhandled message: " + message);
            }
        }
    };



    private BuzzerConnectionManager(Application app) {
        app.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    private final GameplayMessageSender mGameplaySender = new InternalSender();

    public GameplayMessageSender gameplaySender(){
        return mGameplaySender;
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
        }

        @Override
        public void onActivityResumed(Activity activity) {
            foregroundActivityCount++;
            if (foregroundActivityCount == 1 && mClient == null && !mHostScanner.isScanning()) {
                Log.d(TAG, "start scan");
                mHostScanner.scanForHost(mScannerListener);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            foregroundActivityCount--;

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (foregroundActivityCount == 0) {
                Log.d(TAG, "stop server");

                mHostScanner.stop();
                mClient.setConnectionListener(null);
                mClient.close();
                mClient = null;

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


    private class InternalListener implements ConnectionEventListener, GameplayEventListener, SceneEventListener{

        public final Set<ConnectionEventListener> connListeners = new HashSet<>();
        public final Set<GameplayEventListener> gameListeners = new HashSet<>();
        public final Set<SceneEventListener> sceneListeners = new HashSet<>();

        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {
            for (ConnectionEventListener l : connListeners) {
                l.onBuzzerConnectivityChange(isConnected);
            }
        }


        @Override
        public void onBuzzInResponse(BuzzInResponse response) {
            for (GameplayEventListener l : gameListeners) {
                l.onBuzzInResponse(response);
            }
        }

        @Override
        public void onSceneInfo(SceneInfoMessage message) {
            scene = message;
            for (SceneEventListener l : sceneListeners) {
                l.onSceneInfo(message);
            }
        }
    }


    private class InternalSender implements GameplayMessageSender {

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
    }

}
