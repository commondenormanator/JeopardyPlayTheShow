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
import com.seismicgames.jeopardyprototype.BuzzerActivity;
import com.seismicgames.jeopardyprototype.Constants;
import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;
import com.seismicgames.jeopardyprototype.buzzer.listeners.ConnectionEventListener;
import com.seismicgames.jeopardyprototype.buzzer.listeners.GameplayEventListener;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.buzzer.message.WagerRequest;
import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.episode.QuestionInfo;
import com.seismicgames.jeopardyprototype.gameplay.ai.AiManager;
import com.seismicgames.jeopardyprototype.gameplay.events.AnswerReadEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.QuestionAskedEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.WagerEvent;
import com.seismicgames.jeopardyprototype.gameplay.score.AnswerJudge;
import com.seismicgames.jeopardyprototype.ui.GameUiManager;
import com.seismicgames.jeopardyprototype.ui.view.game.PlayerView;

/**
 * Created by jduffy on 6/30/16.
 */
public class GameState {


    public enum State{
        WAIT_FOR_CONNECTION, USER_PAUSED, WAIT_FOR_MEDIA_EVENT,
        WAIT_FOR_BUZZ_IN, WAIT_FOR_WAGER_BUZZ_IN,
        WAIT_FOR_USER_ANSWER,  WAIT_FOR_USER_WAGER
    }

    private enum HandlerMessageType{
        BUZZER_CONN_CHANGE,

        QUESTION_ASKED,
        ANSWER_READ,


        BUZZ_IN_TIMEOUT,
        BUZZ_IN_REQUEST,

        USER_ANSWER_TIMEOUT,
        USER_ANSWER_REQUEST,


        WAGER_PROMPT,
        USER_WAGER_TIMEOUT,
        USER_WAGER_REQUEST,

        VOICE_CAPTURE_STATE,

        USER_RESTART,
        USER_JUMP_TO_MARKER,

        MEDIA_COMPLETE


    }


    public interface MediaEventListener{
        void onWager(WagerEvent event);
        boolean onQuestionAsked(QuestionAskedEvent event);
        void onAnswerRead(AnswerReadEvent event);
        void onMediaComplete();
    }

    public interface MediaManager{
        void setEpisodeDetails(EpisodeDetails details);
        void registerListener(MediaEventListener listener);
        void play();
        void pause();
        void reset();
        void seekTo(int timestamp);
        void onActivityResume(Activity a);
        void onActivityPause(Activity a);
        void onActivityDestroy(Activity a);
    }


    private State mState;

    private boolean initialized = false;

    private EpisodeDetails mDetails;

    private BuzzerConnectionManager mBuzzerManager;
    private MediaManager mMediaManager;
    private GameUiManager mGameUiManager;

    private AnswerJudge judge = new AnswerJudge();
    private Player player2 = new Player();
    private Player player3 = new Player();

    private BuzzerHandler mBuzzerHandler = new BuzzerHandler();
    private MediaHandler mMediaHandler = new MediaHandler();

    private BuzzerActivity activity;
    private Handler handler;
    private Handler.Callback callback = new Handler.Callback(){
        @Override
        public boolean handleMessage(Message message) {
            return GameState.this.handleMessage(message);
        }
    };

    public boolean isInitialized(){
        return initialized;
    }

    public void onResume(BuzzerActivity a){
        activity = a;
        if(handler == null) {
            handler = new Handler(Looper.getMainLooper(), callback);
        }

        if(!initialized) return;
        mMediaManager.onActivityResume(a);
    }

    public void onPause(BuzzerActivity a){
        activity = null;

        if(!initialized) return;
        mMediaManager.onActivityPause(a);
    }

    public void onDestroy(BuzzerActivity a){
        if(!initialized) return;

        mBuzzerManager.removeListener((ConnectionEventListener) mBuzzerHandler);
        mBuzzerManager.removeListener((GameplayEventListener) mBuzzerHandler);
        mMediaManager.onActivityDestroy(a);
    }

    public void init(EpisodeDetails details, BuzzerConnectionManager b, MediaManager m, GameUiManager ui){
        mDetails = details;

        mBuzzerManager = b;
        mBuzzerManager.addListener((ConnectionEventListener) mBuzzerHandler);
        mBuzzerManager.addListener((GameplayEventListener) mBuzzerHandler);

        mMediaManager = m;
        mMediaManager.registerListener(mMediaHandler);
        mMediaManager.setEpisodeDetails(details);

        mGameUiManager = ui;
        mGameUiManager.reset();

        judge.setListener(mGameUiManager);


        initialized = true;
    }

    public void startGame(EpisodeDetails details){
        resumeGame();
    }

    ///Game methods begin

    @MainThread
    private void onBuzzerConnChange(boolean isConnected){
        if(isConnected){

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
    private void waitForBuzzIn(QuestionInfo questionInfo){
        //skip daily double answer if no wager was made
        if(questionInfo.type == QuestionInfo.QuestionType.DD && !judge.didWager()){
            onQuestionTimeout();
            return;
        }

        mState = State.WAIT_FOR_BUZZ_IN;

        judge.setCurrentQuestion(questionInfo);

        AiManager.AiBuzzIn(player2, mGameUiManager.player2);
        AiManager.AiBuzzIn(player3, mGameUiManager.player3);

        int buzzInTime = questionInfo.type == QuestionInfo.QuestionType.FJ ? Constants.FinalJeopardyTimeout : Constants.BuzzInTimeout;
        mGameUiManager.showBuzzTimer(buzzInTime);

        //shrink or show custom clue
        if(questionInfo.type == QuestionInfo.QuestionType.DD) {
            mGameUiManager.showCustomClue();
        }else {
            mGameUiManager.shrinkVideo();
        }


        handler.sendMessageDelayed(handler.obtainMessage(HandlerMessageType.BUZZ_IN_TIMEOUT.ordinal()), buzzInTime);
    }

    @MainThread
    private void onQuestionTimeout(){
        mGameUiManager.hideBuzzTimer();
        mGameUiManager.hideAnswerTimer();
        mGameUiManager.hideWagerBuzzIn();
        mGameUiManager.hideCustomClue();
        mGameUiManager.expandVideo();

        if(judge.didBuzzIn()){
            mGameUiManager.player1.setState(PlayerView.State.AnswerLocked);
        }
        AiManager.AiAnswerLock(player2, mGameUiManager.player2);
        AiManager.AiAnswerLock(player3, mGameUiManager.player3);

        if(activity != null) {
            activity.setScene(BuzzerScene.Scene.BUZZER);
            activity.sendSceneInfo();
        }

        resumeGame();
    }

    @MainThread
    private void onUserAnswerRequest(AnswerRequest request){
//        if(mState == State.WAIT_FOR_USER_ANSWER){
            judge.setUserAnswers(request.answers);
            if(request.answers.size() > 0) {
                mGameUiManager.setUserAnswer(request.answers.get(0));
            }
//        }
    }

    @MainThread
    private void onVoiceCaptureState(VoiceCaptureState request){
        if(mState == State.WAIT_FOR_USER_ANSWER) {
            mGameUiManager.onVoiceCaptureState(request.state);
        }
    }

    @MainThread
    private void onAnswerRead(QuestionInfo questionInfo){
        mBuzzerManager.gameplaySender().sendStopVoiceRec();
        judge.scoreAnswer(questionInfo);
        AiManager.AiAnswer(player2, mGameUiManager.player2, questionInfo);
        AiManager.AiAnswer(player3, mGameUiManager.player3, questionInfo);
        mGameUiManager.clearUserAnswer();
        mGameUiManager.showUserAnswer(false);
        mGameUiManager.player1.setState(PlayerView.State.Normal);

    }

    @MainThread
    private void onUserWager(WagerRequest request){
        if(mState == State.WAIT_FOR_USER_WAGER) {
            judge.setWager(request.wager);
            judge.setUserBuzzedIn();
            mGameUiManager.setWagerValue(request.wager);
        }
    }

    @MainThread
    private void onWagerTimeout(){
        mGameUiManager.showWagerTimer(false);
        if(activity != null) {
            activity.setScene(BuzzerScene.Scene.BUZZER);
            activity.sendSceneInfo();
        }

        resumeGame();
    }

    @MainThread
    private void onBuzzIn(){

        if(mState == State.WAIT_FOR_BUZZ_IN ){
            mBuzzerManager.gameplaySender().sendBuzzInResponse(true);
            mState = State.WAIT_FOR_USER_ANSWER;

            handler.removeMessages(HandlerMessageType.BUZZ_IN_TIMEOUT.ordinal());
            mGameUiManager.hideBuzzTimer();

            mGameUiManager.showUserAnswer(true);
            mGameUiManager.showAnswerTimer(Constants.AnswerTimeout);
            handler.sendMessageDelayed(handler.obtainMessage(HandlerMessageType.USER_ANSWER_TIMEOUT.ordinal()), Constants.AnswerTimeout);
            judge.setUserBuzzedIn();


            mGameUiManager.player1.setState(PlayerView.State.BuzzedIn);

        }else if(mState == State.WAIT_FOR_WAGER_BUZZ_IN){
            mBuzzerManager.gameplaySender().sendBuzzInResponse(true);
            mState = State.WAIT_FOR_USER_WAGER;

            handler.removeMessages(HandlerMessageType.BUZZ_IN_TIMEOUT.ordinal());
            mGameUiManager.hideWagerBuzzIn();

            mGameUiManager.showWagerTimer(true);
            handler.sendMessageDelayed(handler.obtainMessage(HandlerMessageType.USER_WAGER_TIMEOUT.ordinal()), Constants.WagerTimeout);
            judge.setUserBuzzedIn();

            mGameUiManager.player1.setState(PlayerView.State.BuzzedIn);

        }else {
            mBuzzerManager.gameplaySender().sendBuzzInResponse(false);

        }

    }

    @MainThread
    private void waitForWagerBuzzIn(QuestionInfo questionInfo){
        mState = State.WAIT_FOR_WAGER_BUZZ_IN;

        judge.setCurrentQuestion(questionInfo);

        mGameUiManager.showWagerBuzzIn(Constants.BuzzInTimeout);
        mGameUiManager.setCustomClueText(questionInfo);

        handler.sendMessageDelayed(handler.obtainMessage(HandlerMessageType.BUZZ_IN_TIMEOUT.ordinal()), Constants.BuzzInTimeout);

        activity.setScene(BuzzerScene.Scene.WAGER);
        activity.sendSceneInfo();
    }


    private void resumeGame(){

        if(mBuzzerManager.isBuzzerConnected()){
            //start playback
            mState = State.WAIT_FOR_MEDIA_EVENT;

            mBuzzerManager.gameplaySender().sendEpisodeMarkers(mDetails.markers);
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

    public void jumpToMarker(int index){
        if(index < 0 || index > mDetails.markers.size() - 1) return;
        mGameUiManager.reset(false);

        mMediaManager.seekTo(mDetails.markers.get(index).timestamp);

        resumeGame();
    }

    private void onMediaComplete(){
        AfterActionReportActivity.ShowPostMatch(activity, judge.getUserScore(), player2.mScore, player3.mScore);
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
                waitForBuzzIn((QuestionInfo) message.obj);
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
            case USER_JUMP_TO_MARKER:
                jumpToMarker((Integer) message.obj);
                break;
            case WAGER_PROMPT:
                waitForWagerBuzzIn((QuestionInfo) message.obj);
                break;
            case USER_WAGER_TIMEOUT:
                onWagerTimeout();
                break;
            case USER_WAGER_REQUEST:
                onUserWager((WagerRequest) message.obj);
                break;
            case MEDIA_COMPLETE:
                onMediaComplete();
                break;

            default:
                Log.w("GAME_EVENT_HANDLER", type.name() + " was not handled.");
        }

        return false;
    }

    private boolean shouldPauseForQuestion(QuestionAskedEvent event){
        return event.questionInfo.type != QuestionInfo.QuestionType.FJ;
    }

    @WorkerThread
    private class MediaHandler implements MediaEventListener {

        @Override
        public void onWager(WagerEvent event) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.WAGER_PROMPT.ordinal(), event.questionInfo));
        }

        @Override
        public boolean onQuestionAsked(QuestionAskedEvent event) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.QUESTION_ASKED.ordinal(), event.questionInfo));
            return shouldPauseForQuestion(event);
        }

        @Override
        public void onAnswerRead(AnswerReadEvent event) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.ANSWER_READ.ordinal(), event.questionInfo));
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

        @Override
        public void onUserWager(WagerRequest request) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.USER_WAGER_REQUEST.ordinal(), request));
        }

        @Override
        public void onMarkerRequest() {
            mBuzzerManager.gameplaySender().sendEpisodeMarkers(mDetails.markers);
        }

        @Override
        public void onUserJumpToMarker(int markerIndex) {
            handler.sendMessage(handler.obtainMessage(HandlerMessageType.USER_JUMP_TO_MARKER.ordinal(), markerIndex));
        }

        @Override
        public void onQuitGameRequest() {
            activity.finish();
        }
    }
}
