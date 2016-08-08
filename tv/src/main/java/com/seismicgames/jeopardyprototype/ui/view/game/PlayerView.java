package com.seismicgames.jeopardyprototype.ui.view.game;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seismicgames.jeopardyprototype.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jduffy on 8/8/16.
 */
public class PlayerView extends RelativeLayout {
    @BindView(R.id.playerAvatar)
    protected ImageView avatar;
    @BindView(R.id.playerRing)
    protected ImageView ring;
    @BindView(R.id.playerScore)
    protected TextView playerScore;
    @BindView(R.id.playerBanner)
    protected ImageView playerBanner;
    @BindView(R.id.playerName)
    protected TextView playerName;

    private int mScore = 0;

    private TransitionDrawable rightDrawable;
    private TransitionDrawable wrongDrawable;

    public PlayerView(Context context) {
        super(context);
        init(context, null);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        inflate(context, R.layout.player_view, this);
        ButterKnife.bind(this);

        if(!isInEditMode()){
            playerName.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/MrSandsfort.ttf"));
        }


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PlayerView,
                0, 0);

        try {
            int avatarRef = a.getResourceId(R.styleable.PlayerView_avatar, 0);
            setAvatar(avatarRef);

            String name = a.getString(R.styleable.PlayerView_playerName);
            setPlayerName(name);
        } finally {
            a.recycle();
        }


        rightDrawable = (TransitionDrawable) context.getResources().getDrawable(R.drawable.player_banner_correct_trans);
        if(rightDrawable != null) rightDrawable.setCrossFadeEnabled(true);
        wrongDrawable = (TransitionDrawable) context.getResources().getDrawable(R.drawable.player_banner_wrong_trans);
        if(wrongDrawable != null) wrongDrawable.setCrossFadeEnabled(true);
    }

    public void setAvatar(int id){
        avatar.setImageResource(id);
    }

    public void setPlayerName(String name){
        playerName.setText(name);
    }

    public void setPlayerScore(int score) {

        playerScore.setText(String.format("$%s", new DecimalFormat("#,###").format(score)));

        TransitionDrawable drawable = score >= mScore ? rightDrawable : wrongDrawable;
        playerBanner.setImageDrawable(drawable);
        drawable.startTransition(2000);

        mScore = score;
    }

    public void setState(State state){
        switch(state){

            case Normal:
                ring.setVisibility(GONE);
                break;
            case BuzzedIn:
                ring.setVisibility(VISIBLE);
                ring.setImageResource(R.drawable.player_buzz);
                break;
            case AnswerLocked:
                ring.setImageResource(R.drawable.player_buzz_locked);
                break;
        }
    }

    public enum State {
        Normal, BuzzedIn, AnswerLocked
    }

}
