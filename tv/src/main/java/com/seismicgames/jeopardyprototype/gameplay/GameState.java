package com.seismicgames.jeopardyprototype.gameplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.widget.Toast;

import com.seismicgames.jeopardyprototype.AfterActionReportActivity;
import com.seismicgames.jeopardyprototype.Constants;
import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.listeners.ConnectionEventListener;
import com.seismicgames.jeopardyprototype.buzzer.listeners.GameplayEventListener;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.episode.QuestionInfo;
import com.seismicgames.jeopardyprototype.gameplay.score.AnswerJudge;
import com.seismicgames.jeopardyprototype.ui.GameUiManager;

/**
 * Created by jduffy on 6/30/16.
 */
public class GameState {


    public enum State{
        WAIT_FOR_CONNECTION, USER_PAUSED, WAIT_FOR_MEDIA_EVENT, WAIT_FOR_BUZZ_IN, WAIT_FOR_USER_RESPONSE
    }

    private enum HandlerMessageType{
        BUZZER_CONN_CHANGE,
        QUESTION_ASKED,

        BUZZ_IN_TIMEOUT,
        BUZZ_IN_REQUEST,

        USER_ANSWER_TIMEOUT,
        USER_ANSWER_REQUEST,

        ANSWER_READ,

        VOICE_CAPTURE_STATE,

        USER_RESTART,

        MEDIA_COMPLETE

    }


    public interface MediaEventListener{
        void onQuestionAsked();
        void onAnswerRead(QuestionInfo info);
        void onMediaComplete();
    }

    public interface MediaManager{
        void registerListener(MediaEventListener listener);
        void play();
        void pause();
        void reset();
        void onActivityResume(Activity a);
        void onActivityPause(Activity a);
        void onActivityDestroy(Activity a);
    }


    private State mState;

    private BuzzerConnectionManager mBuzzerManager;
    private MediaManager mMediaManager;
    private GameUiManager mGameUiManager;

    private AnswerJudge judge = new AnswerJudge();

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
        if(handler == null) {
            handler = new Handler(Looper.getMainLooper(), callback);
        }
        mMediaManager.onActivityResume(a);
    }

    public void onPause(Activity a){
        mMediaManager.onActivityPause(a);
        activity = null;
    }

    public void onDestroy(Activity a){
        mBuzzerManager.removeListener((ConnectionEventListener) mBuzzerHandler);
        mBuzzerManager.removeListener((GameplayEventListener) mBuzzerHandler);
        mMediaManager.onActivityDestroy(a);
    }

    public void init(BuzzerConnectionManager b, MediaManager m, GameUiManager ui){
        mBuzzerManager = b;
        mBuzzerManager.addListener((ConnectionEventListener) mBuzzerHandler);
        mBuzzerManager.addListener((GameplayEventListener) mBuzzerHandler);

        mMediaManager = m;
        mMediaManager.registerListener(mMediaHandler);

        mGameUiManager = ui;

        judge.setListener(mGameUiManager);
    }

    public void startGame(EpisodeDetails details){
        resumeGame();
    }

    ///Game methods begin

    @MainThread
    private void onBuzzerConnChange(boolean isConnected){
        if(isConnected){
//            mGameUiManager.showDisconnectWarning(false);

            if(activity != null) {
                Toast.makeText(activity, "connected", Toast.LENGTH_LONG).show();
            }

            resumeGame();
        } else {
            waitForBuzzerConnect();
        }
    }
    @MainThread
    private void waitForBuzzerConnect(){
//        if(mState==State.WAIT_FOR_MEDIA_EVENT){
//            mGameUiManager.showDisconnectWarning(true);
//        }

        mState = State.WAIT_FOR_CONNECTION;
        pauseGame();

        if(activity != null)
            Toast.makeText(activity, "please connect", Toast.LENGTH_LONG).show();
    }

    @MainThread
    private void waitForBuzzIn(){
        mState = State.WAIT_FOR_BUZZ_IN;

        mGameUiManager.showBuzzTimer(Constants.BuzzInTimeout);
        handler.sendMessageDelayed(handler.obtainMessage(HandlerMessageType.BUZZ_IN_TIMEOUT.ordinal()), Constants.BuzzInTimeout);
    }

    @MainThread
    private void onQuestionTimeout(){
        mGameUiManager.hideBuzzTimer();
        mGameUiManager.hideAnswerTimer();

        resumeGame();
    }

    @MainThread
    private void onUserAnswerRequest(AnswerRequest request){
        if(mState == State.WAIT_FOR_USER_RESPONSE){
            judge.setUserAnswers(request.answers);
            if(request.answers.size() > 0) {
                mGameUiManager.setUserAnswer(request.answers.get(0));
            }
        }
    }

    @MainThread
    private void onVoiceCaptureState(VoiceCaptureState request){
        mGameUiManager.onVoiceCaptureState(request.state);
    }

    @MainThread
    private void onAnswerRead(QuestionInfo questionInfo){
        judge.scoreAnswer(questionInfo);
        mGameUiManager.clearUserAnswer();
    }

    @MainThread
    private void onBuzzIn(){
        if(mState != State.WAIT_FOR_BUZZ_IN){
            mBuzzerManager.GameplaySender().sendBuzzInResponse(false);
            return;
        }
        mBuzzerManager.GameplaySender().sendBuzzInResponse(true);
        mState = State.WAIT_FOR_USER_RESPONSE;

        handler.removeMessages(HandlerMessageType.BUZZ_IN_TIMEOUT.ordinal());
        mGameUiManager.hideBuzzTimer();

        mGameUiManager.showAnswerTimer(Constants.AnswerTimeout);
        handler.sendMessageDelayed(handler.obtainMessage(HandlerMessageType.USER_ANSWER_TIMEOUT.ordinal()), Constants.AnswerTimeout);
        judge.setUserBuzzedIn();



    }


    private void resumeGame(){
        if(mBuzzerManager.isBuzzerConnected()){
            //start playback
            mState = State.WAIT_FOR_MEDIA_EVENT;
            mMediaManager.play();
        } else {

//            mGameUiManager.showDisconnectWarning(true);

            //wait for connection
            waitForBuzzerConnect();
        }
    }


    private void pauseGame(){
        mMediaManager.pause();
    }

    ///Game methods end

    public void restartGame(){
        mMediaManager.reset();
        judge.reset();
        mGameUiManager.reset();
        resumeGame();
    }

    private void onMediaComplete(){
        AfterActionReportActivity.ShowPostMatch(activity, judge.getUserScore());
        activity.finish();
    }


    @MainThread
    private boolean handleMessage(Message message){
        HandlerMessageType type = HandlerMessageType.values()[message.what];
        //Toast.makeText(activity, type.toString(), Toast.LENGTH_SHORT).show();
        switch (type){

            case BUZZER_CONN_CHANGE:
                onBuzzerConnChange((Boolean)message.obj);
                break;
            case QUESTION_ASKED:
                waitForBuzzIn();
                break;
            case ANSWER_READ:
                onAnswerRead((QuestionInfo) message.obj);
                break;
            case BUZZ_IN_TIMEOUT:
                onQuestionTimeout();
                break;
            case BUZZ_IN_REQUEST:
                onBuzzIn();
                break;
            case USER_ANSWER_TIMEOUT:
                onQuestionTimeout();
                break;
            case USER_ANSWER_REQUEST:
                onUserAnswerRequest((AnswerRequest) message.obj);
                break;
            case VOICE_CAPTURE_STATE:
                onVoiceCaptureState((VoiceCaptureState) message.obj);
                break;
            case USER_RESTART:
                restartGame();
                break;
            case MEDIA_COMPLETE:
                onMediaComplete();
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
        public void onAnswerRead(QuestionInfo info) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.ANSWER_READ.ordinal(), info));
        }

        @Override
        public void onMediaComplete(){
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.MEDIA_COMPLETE.ordinal()));
        }
    }

    @WorkerThread
    private class BuzzerHandler implements GameplayEventListener, ConnectionEventListener
    {

        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.BUZZER_CONN_CHANGE.ordinal(), isConnected));
        }

        @Override
        public void onUserBuzzIn() {
            handler.sendEmptyMessage(HandlerMessageType.BUZZ_IN_REQUEST.ordinal());
        }

        @Override
        public void onUserAnswer(AnswerRequest request) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.USER_ANSWER_REQUEST.ordinal(), request));
        }

        @Override
        public void onVoiceCaptureState(VoiceCaptureState request) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.VOICE_CAPTURE_STATE.ordinal(), request));
        }

        @Override
        public void onUserRestart() {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.USER_RESTART.ordinal()));
        }
    }
}
