package com.seismicgames.jeopardyprototype.buzzer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seismicgames.jeopardyprototype.buzzer.listeners.ConnectionEventListener;
import com.seismicgames.jeopardyprototype.buzzer.listeners.GameplayEventListener;
import com.seismicgames.jeopardyprototype.buzzer.listeners.RemoteEventListener;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.RemoteKeyMessage;
import com.seismicgames.jeopardyprototype.buzzer.message.SceneInfoMessage;
import com.seismicgames.jeopardyprototype.buzzer.message.StopVoiceRecRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.buzzer.message.WagerRequest;
import com.seismicgames.jeopardyprototype.buzzer.sender.GameplayMessageSender;
import com.seismicgames.jeopardyprototype.buzzer.sender.SceneMessageSender;

import java.io.IOException;
import java.util.HashSet;
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

    private final InternalListener mListener = new InternalListener();

    private BuzzerServer mServer;

    private final Gson gson = new Gson();

    private final BuzzerServerListener mServerListener = new BuzzerServerListener() {
        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {
            mListener.onBuzzerConnectivityChange(isConnected);
        }

        @Override
        public void onMessage(String message) {
            try {
                JsonObject json = gson.fromJson(message, JsonObject.class);

                switch (json.get("type").getAsString()) {
                    case "BuzzInRequest":
                        mListener.onUserBuzzIn();
                        break;
                    case "AnswerRequest":
                        mListener.onUserAnswer(gson.fromJson(json, AnswerRequest.class));
                        break;
                    case "RestartRequest":
                        mListener.onUserRestart();
                        break;
                    case "VoiceCaptureState":
                        mListener.onVoiceCaptureState(gson.fromJson(json, VoiceCaptureState.class));
                        break;
                    case "RemoteKeyMessage":
                        mListener.onKeyEvent(gson.fromJson(json, RemoteKeyMessage.class));
                        break;
                    case "WagerRequest":
                        mListener.onUserWager(gson.fromJson(json, WagerRequest.class));
                        break;
                    default:
                        Log.e(TAG, "Unhandled message: " + message);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private final InternalSender mSender = new InternalSender();

    private BuzzerConnectionManager(Application app) {
        Application.ActivityLifecycleCallbacks lifecycleCallbacks = new LifecycleCallbacks();
        app.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public GameplayMessageSender gameplaySender(){
        return mSender;
    }
    public SceneMessageSender sceneSender(){
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

    public void addListener(RemoteEventListener listener){
        mListener.remoteListeners.add(listener);
    }
    public void removeListener(RemoteEventListener listener){
        mListener.remoteListeners.remove(listener);
    }

    public boolean isBuzzerConnected() {
        return mServer != null && mServer.isBuzzerConnected();
    }


    private class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        private int foregroundActivityCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            foregroundActivityCount++;
            if (foregroundActivityCount == 1 && mServer == null) {
                Log.d(TAG, "start server");
                mServer = new BuzzerServer();
                mServer.registerListener(mServerListener);
                mServer.start();
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
                try {
                    mServer.stop();
                    mServer.unregisterListener(mServerListener);
                    mServer = null;
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
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



    private class InternalListener implements ConnectionEventListener, GameplayEventListener, RemoteEventListener {

        public final Set<ConnectionEventListener> connListeners = new HashSet<>();
        public final Set<GameplayEventListener> gameListeners = new HashSet<>();
        public final Set<RemoteEventListener> remoteListeners = new HashSet<>();

        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {
            for (ConnectionEventListener l : connListeners) {
                l.onBuzzerConnectivityChange(isConnected);
            }
        }

        @Override
        public void onUserBuzzIn() {
            for (GameplayEventListener l : gameListeners) {
                l.onUserBuzzIn();
            }
        }

        @Override
        public void onUserRestart() {
            for (GameplayEventListener l : gameListeners) {
                l.onUserRestart();
            }
        }

        @Override
        public void onUserAnswer(AnswerRequest request) {
            for (GameplayEventListener l : gameListeners) {
                l.onUserAnswer(request);
            }
        }

        @Override
        public void onVoiceCaptureState(VoiceCaptureState request) {
            for (GameplayEventListener l : gameListeners) {
                l.onVoiceCaptureState(request);
            }
        }

        @Override
        public void onUserWager(WagerRequest request) {
            for (GameplayEventListener l : gameListeners) {
                l.onUserWager(request);
            }
        }

        @Override
        public void onKeyEvent(RemoteKeyMessage message) {
            for (RemoteEventListener l : remoteListeners) {
                l.onKeyEvent(message);
            }
        }
    }


    private class InternalSender implements GameplayMessageSender, SceneMessageSender {

        private void sendMessage(String message){
            if (mServer != null) {
                mServer.sendMessage(message);
            }
        }

        @Override
        public void sendBuzzInResponse(boolean isValidBuzz) {
            sendMessage(gson.toJson(new BuzzInResponse(isValidBuzz)));
        }

        @Override
        public void sendStopVoiceRec() {
            sendMessage(gson.toJson(new StopVoiceRecRequest()));
        }

        @Override
        public void sendSceneInfo(String scene) {
            sendMessage(gson.toJson(new SceneInfoMessage(scene)));
        }
    }

}
