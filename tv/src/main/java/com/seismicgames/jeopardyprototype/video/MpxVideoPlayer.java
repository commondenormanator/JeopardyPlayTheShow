package com.seismicgames.jeopardyprototype.video;

import com.theplatform.adk.Player;

/**
 * Created by jduffy on 6/29/16.
 */
public class MpxVideoPlayer implements VideoPlayer {

    private Player mPlayer;


    public MpxVideoPlayer(Player mPlayer) {
        this.mPlayer = mPlayer;
    }

    @Override
    public void play() {
        mPlayer.asMediaPlayerControl().start();
    }

    @Override
    public void pause() {
        mPlayer.asMediaPlayerControl().pause();
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.asMediaPlayerControl().getCurrentPosition();
    }

}
