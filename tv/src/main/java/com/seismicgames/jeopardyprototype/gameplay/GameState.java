package com.seismicgames.jeopardyprototype.gameplay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.widget.Toast;

import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.video.VideoPlayer;

/**
 * Created by jduffy on 6/30/16.
 */
public class GameState {


    public enum State{
        WAIT_FOR_CONNECTION, USER_PAUSED, WAIT_FOR_MEDIA_EVENT, WAIT_FOR_BUZZ_IN, WAIT_FOR_USER_RESPONSE
    }

    private enum HandlerMessageType{
        BUZZER_CONN_CHANGE, QUESTION_ASKED, BUZZ_IN_TIMEOUT, USER_ANSWER_TIMEOUT, ANSWER_READ
    }


    public interface MediaEventListener{
        void onQuestionAsked();
        void onAnswerRead();
    }

    public interface MediaManager{
        void registerListener(MediaEventListener listener);
        void play();
        void pause();
    }

    public interface BuzzerManager{
        void registerListener(BuzzerEventListener listener);
        boolean isBuzzerConnected();
    }

    public interface BuzzerEventListener{
        void onBuzzerConnectivityChange(boolean isConnected);

        void onUserBuzzIn();
        void onUserAnswer();
    }

    private State mState;

    private BuzzerManager mBuzzerManager;
    private MediaManager mMediaManager;

    private BuzzerHandler mBuzzerHandler = new BuzzerHandler();
    private MediaHandler mMediaHandler = new MediaHandler();

    private Activity activity;
    private Handler handler;
    private Handler.Callback callback = new Handler.Callback(){
        @Override
        public boolean handleMessage(Message message) {
            return GameState.this.handleMessage(message);
        }
    };

    public void onResume(Activity a){
        activity = a;
        handler = new Handler(Looper.getMainLooper(), callback);

    }

    public void onPause(Activity a){
        activity = null;
    }

    public void init(BuzzerManager b, MediaManager m){
        mBuzzerManager = b;
        mBuzzerManager.registerListener(mBuzzerHandler);

        mMediaManager = m;
        mMediaManager.registerListener(mMediaHandler);
    }

    public void startGame(EpisodeDetails details){
        resumeGame();
    }

    ///Game methods begin

    @MainThread
    private void onBuzzerConnChange(boolean isConnected){
        if(isConnected){
            Toast.makeText(activity, "connected", Toast.LENGTH_LONG).show();

            resumeGame();
        } else {
            waitForBuzzerConnect();
        }
    }
    @MainThread
    private void waitForBuzzerConnect(){
        mState = State.WAIT_FOR_CONNECTION;
        pauseGame();

        Toast.makeText(activity, "please connect", Toast.LENGTH_LONG).show();
    }

    @MainThread
    private void waitForBuzzIn(){
        mState = State.WAIT_FOR_BUZZ_IN;

        handler.sendMessageDelayed(handler.obtainMessage(HandlerMessageType.BUZZ_IN_TIMEOUT.ordinal()), 10000);
    }

    @MainThread
    private void onBuzzTimeout(){
        mState = State.WAIT_FOR_MEDIA_EVENT;
        mMediaManager.play();
    }


    private void resumeGame(){
        if(mBuzzerManager.isBuzzerConnected()){
            //start playback
            mState = State.WAIT_FOR_MEDIA_EVENT;
            mMediaManager.play();
        } else {
            //wait for connection
            waitForBuzzerConnect();
        }
    }


    private void pauseGame(){
        mMediaManager.pause();
    }

    ///Game methods end



    @MainThread
    private boolean handleMessage(Message message){
        HandlerMessageType type = HandlerMessageType.values()[message.what];
        Toast.makeText(activity, type.toString(), Toast.LENGTH_SHORT).show();
        switch (type){

            case BUZZER_CONN_CHANGE:
                onBuzzerConnChange((Boolean)message.obj);
                break;
            case QUESTION_ASKED:
                waitForBuzzIn();
                break;
            case BUZZ_IN_TIMEOUT:
                onBuzzTimeout();
                break;
            default:
                Log.w("GAME_EVENT_HANDLER", type.name() + " was not handled.");
        }


        return false;
    }


    @WorkerThread
    private class MediaHandler implements MediaEventListener {

        @Override
        public void onQuestionAsked() {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.QUESTION_ASKED.ordinal()));
        }

        @Override
        public void onAnswerRead() {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.ANSWER_READ.ordinal()));
        }
    }

    @WorkerThread
    private class BuzzerHandler implements BuzzerEventListener
    {

        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.BUZZER_CONN_CHANGE.ordinal(), isConnected));
        }

        @Override
        public void onUserBuzzIn() {

        }

        @Override
        public void onUserAnswer() {

        }
    }
}
