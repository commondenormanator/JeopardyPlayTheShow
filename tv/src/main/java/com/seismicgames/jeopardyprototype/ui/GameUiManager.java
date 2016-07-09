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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seismicgames.jeopardyprototype.R;
import com.seismicgames.jeopardyprototype.gameplay.score.ScoreChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jduffy on 7/6/16.
 */
public class GameUiManager implements ScoreChangeListener{
    @BindView(R.id.buzzerTimer)
    public ProgressBar buzzerTimer;

    @BindView(R.id.answerTimer)
    public ProgressBar answerTimer;

    @BindView(R.id.userScore)
    public TextView userScore;

    public GameUiManager(Activity view) {
        ButterKnife.bind(this, view);
        buzzerTimer.setVisibility(View.INVISIBLE);
        answerTimer.setVisibility(View.INVISIBLE);
    }

    public void showBuzzTimer(int duration) {
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
//                buzzerTimer.setProgress(0);
                stopAnim(buzzerTimer);
            }
        });
        storeAnim(buzzerTimer, anim);
        anim.start();
    }
    public void hideBuzzTimer(){
        stopAnim(buzzerTimer);

        buzzerTimer.setVisibility(View.INVISIBLE);
//        buzzerTimer.setProgress(0);
    }

    public void showAnswerTimer(int duration) {
        stopAnim(answerTimer);

        answerTimer.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofInt(answerTimer, "progress", 100, 0);
        anim.setDuration(duration);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                answerTimer.setVisibility(View.INVISIBLE);
//                answerTimer.setProgress(0);

                stopAnim(answerTimer);
            }
        });
        anim.start();
        storeAnim(answerTimer, anim);
    }

    public void hideAnswerTimer(){
        stopAnim(answerTimer);

        answerTimer.setVisibility(View.INVISIBLE);
//        answerTimer.setProgress(0);
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
        v.setTag(R.id.buzzerTimer, animation);
    }

    private void stopAnim(View v){
        ValueAnimator anim = ((ValueAnimator)v.getTag(R.id.buzzerTimer));
        v.setTag(R.id.buzzerTimer, null);

        if(anim != null && anim.isRunning()){
            anim.end();
        }
    }
}
