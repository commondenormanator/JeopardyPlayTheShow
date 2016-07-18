package com.seismicgames.jeopardyprototype.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seismicgames.jeopardyprototype.R;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.gameplay.score.ScoreChangeListener;
import com.seismicgames.jeopardyprototype.view.AnswerTimer;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jduffy on 7/6/16.
 */
public class GameUiManager implements ScoreChangeListener{
    @BindView(R.id.buzzerTimerLeft)
    public ProgressBar buzzerTimerLeft;
    @BindView(R.id.buzzerTimerRight)
    public ProgressBar buzzerTimerRight;

    @BindView(R.id.userScore)
    public TextView userScore;

    @BindView(R.id.podiumTimer)
    public AnswerTimer answerTimer;

    @BindView(R.id.disconnectedWarning)
    public View disconnectWarning;

    @BindView(R.id.videoContainer)
    public View videoContainer;

    @BindView(R.id.userAnswer)
    public TextView userAnswer;

    @BindView(R.id.micIcon)
    public ImageView micIcon;

    public GameUiManager(Activity view) {
        ButterKnife.bind(this, view);
        buzzerTimerLeft.setVisibility(View.INVISIBLE);
        buzzerTimerRight.setVisibility(View.INVISIBLE);
    }

    public void showDisconnectWarning(boolean show){
        if(show) {
            disconnectWarning.setVisibility(View.VISIBLE);
            disconnectWarning.getParent().requestTransparentRegion(disconnectWarning);
        }else {
            disconnectWarning.setVisibility(View.INVISIBLE);
        }

    }

    public void showBuzzTimer(int duration) {
        showBuzzTimer(buzzerTimerLeft, duration);
        showBuzzTimer(buzzerTimerRight, duration);
    }
    public void hideBuzzTimer(){
       hideBuzzTimer(buzzerTimerLeft);
        hideBuzzTimer(buzzerTimerRight);
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




    public void showAnswerTimer(int duration) {
        answerTimer.start(duration);
    }

    public void hideAnswerTimer(){
        answerTimer.cancel();
    }

    @Override
    public void onScoreChange(final int score, int change) {
        userScore.setText((change > 0 ? "+" : "-") + String.valueOf(Math.abs(change)));
        userScore.setTextColor(change > 0 ? Color.GREEN : Color.RED);
        userScore.animate().translationYBy(-1 * Math.copySign(100, change)).withEndAction(new Runnable() {
            @Override
            public void run() {
                userScore.setTextColor(Color.WHITE);
                userScore.setTranslationY(0);
                userScore.setText(String.valueOf(score));
            }
        }).start();
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
                micIcon.setImageResource(R.drawable.lb_ic_search_mic_out);
                break;
            case SPEECH_BEGIN:
                micIcon.setVisibility(View.VISIBLE);
                micIcon.setImageResource(R.drawable.lb_ic_search_mic);
                break;
            case SPEECH_END:
                micIcon.setVisibility(View.GONE);
                break;
            case RECOGNITION_COMPLETE:
                micIcon.setVisibility(View.GONE);
                break;
        }
    }


    public void reset(){
        hideAnswerTimer();
        hideBuzzTimer();
        userScore.setText("");
    }

}
