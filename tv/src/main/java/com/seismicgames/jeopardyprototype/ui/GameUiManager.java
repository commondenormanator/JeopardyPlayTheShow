package com.seismicgames.jeopardyprototype.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seismicgames.jeopardyprototype.R;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.episode.QuestionInfo;
import com.seismicgames.jeopardyprototype.gameplay.score.ScoreChangeListener;
import com.seismicgames.jeopardyprototype.ui.view.game.PlayerView;
import com.seismicgames.jeopardyprototype.view.AnswerTimer;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jduffy on 7/6/16.
 */
public class GameUiManager implements ScoreChangeListener{

    @BindView(R.id.buzzerTimer)
    public ProgressBar buzzerTimer;

    @BindView(R.id.podiumTimer)
    public AnswerTimer answerTimer;

    @BindView(R.id.videoContainer)
    public View videoContainer;

    @BindView(R.id.userAnswer)
    public TextView userAnswer;

    @BindView(R.id.micIcon)
    public ImageView micIcon;


    @BindView(R.id.wager_layout)
    public View wagerView;
    @BindView(R.id.wagerValue)
    public TextView wagerValue;

    @BindView(R.id.wagerPrompt)
    public View wagerPrompt;

    @BindView(R.id.clueLayout)
    public View clueLayout;
    @BindView(R.id.clueText)
    public TextView clueText;
    @BindView(R.id.clueShadow)
    public TextView clueShadow;

    @BindView(R.id.playerLayout)
    public ViewGroup playerLayout;

    @BindView(R.id.player1)
    public PlayerView player1;
    @BindView(R.id.player2)
    public PlayerView player2;
    @BindView(R.id.player3)
    public PlayerView player3;


    @BindView(R.id.homePlayerSplash)
    public View homePlayerSplash;

    private MediaPlayer applause;

    public GameUiManager(Activity view) {
        ButterKnife.bind(this, view);
        buzzerTimer.setVisibility(View.INVISIBLE);
        Typeface tf = Typeface.createFromAsset(view.getAssets(), "fonts/clue.ttf");
        clueText.setTypeface(tf);
        clueShadow.setTypeface(tf);

        applause = MediaPlayer.create(view, R.raw.applause);
        applause.setLooping(true);
    }


    public void showBuzzTimer(int duration) {
        showBuzzTimer(buzzerTimer, duration);
    }

    public void hideBuzzTimer() {
        hideBuzzTimer(buzzerTimer);
    }

    private void showBuzzTimer(final ProgressBar buzzerTimer, int duration) {
        stopAnim(buzzerTimer);

        buzzerTimer.setVisibility(View.VISIBLE);
        buzzerTimer.getParent().requestTransparentRegion(buzzerTimer);

        ((ViewGroup)buzzerTimer.getParent()).invalidate();
        ObjectAnimator anim = ObjectAnimator.ofInt(buzzerTimer, "progress", 100, 0);
        anim.setDuration(duration);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                buzzerTimer.setVisibility(View.INVISIBLE);
                stopAnim(buzzerTimer);
            }
        });
        storeAnim(buzzerTimer, anim);
        anim.start();
    }
    private void hideBuzzTimer(ProgressBar buzzerTimer){
        stopAnim(buzzerTimer);

        buzzerTimer.setVisibility(View.INVISIBLE);
    }

    public void showWagerBuzzIn(int duration) {
        showBuzzTimer(duration);
        wagerPrompt.setVisibility(View.VISIBLE);
    }
    public void hideWagerBuzzIn() {
        hideBuzzTimer();
        wagerPrompt.setVisibility(View.GONE);
    }

    public void showWagerTimer(boolean show){
        if(show) {
            setWagerValue(0);
            wagerView.setVisibility(View.VISIBLE);
        } else {
            wagerView.setVisibility(View.GONE);

        }
    }

    public void setWagerValue(int value){
        wagerValue.setText(String.format("$%5d", value));
    }


    public void showCustomClue(){
        clueLayout.setVisibility(View.VISIBLE);
    }

    public void setCustomClueText(QuestionInfo info){
        clueText.setText(info.clue);
        clueShadow.setText(info.clue);
    }

    public void hideCustomClue(){
        clueLayout.setVisibility(View.GONE);
    }

    public void showAnswerTimer(int duration) {
        answerTimer.start(duration);
    }

    public void hideAnswerTimer(){
        answerTimer.cancel();
    }

    @Override
    public void onScoreChange(final int score, int change) {
        scoreChange(player1, score, change);
    }

    private void scoreChange(PlayerView player, int score, int change){
        player.setPlayerScore(score);
    }

    private void storeAnim(View v, ValueAnimator animation){
        v.setTag(R.id.anim_tag, animation);
    }

    private void stopAnim(View v){
        ValueAnimator anim = ((ValueAnimator)v.getTag(R.id.anim_tag));
        v.setTag(R.id.anim_tag, null);

        if(anim != null && anim.isRunning()){
            anim.end();
        }
    }

    public void showUserAnswer(boolean show){
        userAnswer.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void setUserAnswer(String s){
        userAnswer.setText(s);
    }

    public void clearUserAnswer(){
        userAnswer.setText("");
        micIcon.setVisibility(View.GONE);
    }


    public void onVoiceCaptureState(VoiceCaptureState.State state){
        switch (state){

            case LISTENING:
                micIcon.setVisibility(View.VISIBLE);
                micIcon.setImageResource(R.drawable.voice_ready_icn);
                break;
            case SPEECH_BEGIN:
                micIcon.setVisibility(View.VISIBLE);
                micIcon.setImageResource(R.drawable.voice_heard_icn);
                break;
            case SPEECH_END:
                micIcon.setVisibility(View.GONE);
                break;
            case RECOGNITION_COMPLETE:
                micIcon.setVisibility(View.GONE);
                break;
        }
    }

    public void showPlayers(){
        playerLayout.setVisibility(View.VISIBLE);
    }

    public void showHomePlayerSplash(final boolean show) {
        if (show){
            homePlayerSplash.setVisibility(View.VISIBLE);
            applause.start();
        }else {
            applause.stop();
        }

        homePlayerSplash
                .animate()
                .setDuration(1000)
                .alpha(show ? 1 : 0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        homePlayerSplash.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                }).start();

    }


    public void reset(){
        reset(true);
    }
    public void reset(boolean resetScore){
        hideAnswerTimer();
        hideBuzzTimer();
        expandVideo();
        if(resetScore) {
            player1.setPlayerScore(0, false);
            player2.setPlayerScore(0, false);
            player3.setPlayerScore(0, false);
        }
    }


    public void shrinkVideo(){
        ValueAnimator animator = ValueAnimator.ofInt(0, answerTimer.getHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                int vertical = (Integer) valueAnimator.getAnimatedValue() / 2;
                int horizontalPadding = (((Integer) valueAnimator.getAnimatedValue() * 16) / 9) /2;
                videoContainer.setPadding(horizontalPadding, vertical, horizontalPadding, vertical);
                videoContainer.setTranslationY(-vertical);
            }
        });
        animator.setDuration(300);
        animator.start();
    }

    public void expandVideo(){
        ValueAnimator animator = ValueAnimator.ofInt(videoContainer.getPaddingTop() + videoContainer.getPaddingBottom(), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                int vertical = (Integer) valueAnimator.getAnimatedValue() / 2;
                int horizontalPadding = (((Integer) valueAnimator.getAnimatedValue() * 16) / 9) /2;
                videoContainer.setPadding(horizontalPadding, vertical, horizontalPadding, vertical);
                videoContainer.setTranslationY(-vertical);
            }
        });
        animator.setDuration(300);
        animator.start();
    }
}
