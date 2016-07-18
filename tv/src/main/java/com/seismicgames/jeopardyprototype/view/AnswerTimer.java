package com.seismicgames.jeopardyprototype.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.seismicgames.jeopardyprototype.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jduffy on 7/12/16.
 */
public class AnswerTimer extends LinearLayout {

    private final HideTimerRunnable mHideTimerRunnable = new HideTimerRunnable();
    private final DeactivateRunnable mDeactivateRunnable = new DeactivateRunnable();

    private int state;
    private List<View> lights = new ArrayList<>();

    @BindView(R.id.light0) protected View light0;
    @BindView(R.id.light1) protected View light1;
    @BindView(R.id.light2) protected View light2;
    @BindView(R.id.light3) protected View light3;
    @BindView(R.id.light4) protected View light4;
    @BindView(R.id.light5) protected View light5;
    @BindView(R.id.light6) protected View light6;
    @BindView(R.id.light7) protected View light7;
    @BindView(R.id.light8) protected View light8;

    public AnswerTimer(Context context) {
        this(context, null);
    }

    public AnswerTimer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnswerTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnswerTimer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.answer_timer, this, true);
        ButterKnife.bind(this);
        lights.add(light0);
        lights.add(light1);
        lights.add(light2);
        lights.add(light3);
        lights.add(light4);
        lights.add(light5);
        lights.add(light6);
        lights.add(light7);
        lights.add(light8);
    }

    public void start(int duration) {
        setVisibility(View.VISIBLE);
        getParent().requestTransparentRegion(this);
        state = 5;
        for(View v : lights){
            v.setActivated(true);
        }
        postDelayed(mDeactivateRunnable, duration / 5);
        postDelayed(mDeactivateRunnable, 2 * duration / 5);
        postDelayed(mDeactivateRunnable, 3 * duration / 5);
        postDelayed(mDeactivateRunnable, 4 * duration / 5);
        postDelayed(mDeactivateRunnable, duration);
        postDelayed(mHideTimerRunnable, duration+500);

    }

    public void cancel() {
        onEnd();
    }

    private void onEnd() {
        removeCallbacks(mHideTimerRunnable);
        removeCallbacks(mDeactivateRunnable);
        setVisibility(View.INVISIBLE);
    }


    private class HideTimerRunnable implements Runnable {

        @Override
        public void run() {
            onEnd();
        }
    }

    private class DeactivateRunnable implements Runnable {

        @Override
        public void run() {

            state--;

            switch (state) {
                case 4:
                    lights.get(0).setActivated(false);
                    lights.get(8).setActivated(false);
                    break;
                case 3:
                    lights.get(1).setActivated(false);
                    lights.get(7).setActivated(false);
                    break;
                case 2:
                    lights.get(2).setActivated(false);
                    lights.get(6).setActivated(false);
                    break;
                case 1:
                    lights.get(3).setActivated(false);
                    lights.get(5).setActivated(false);
                    break;
                case 0:
                    lights.get(4).setActivated(false);
                    break;
            }
        }
    }

}
