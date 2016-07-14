package com.seismicgames.jeopardyprototype.video;

import android.app.Activity;
import android.util.Log;
import android.widget.MediaController;

import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.gameplay.GameState;
import com.seismicgames.jeopardyprototype.gameplay.events.AnswerReadEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeEvent;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jduffy on 7/1/16.
 */
public class MediaPlayerControlMediaManager implements GameState.MediaManager {

    private static final String TAG = MediaPlayerControlMediaManager.class.getName();

    private MediaController.MediaPlayerControl mPlayer;
    private EpisodeDetails mDetails;

    private GameState.MediaEventListener mListener;

    private int mEpisodeEventIndex;

    private boolean disposed;

    private Thread thread =  new Thread(){
        @Override
        public void run() {
            while(!disposed){

                checkForEvent();


                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private volatile boolean shouldPlay;


    public MediaPlayerControlMediaManager(MediaController.MediaPlayerControl player, EpisodeDetails details) {
        this.mPlayer = player;
        this.mDetails = details;

        thread.start();

    }

    @Override
    public void registerListener(GameState.MediaEventListener listener) {
        mListener = listener;
    }


    @Override
    public void play() {
        shouldPlay = true;
        mPlayer.start();

    }

    @Override
    public void pause() {
        mPlayer.pause();

    }

    @Override
    public void onActivityResume(Activity a) {

    }

    @Override
    public void onActivityPause(Activity a) {

    }

    @Override
    public void onActivityDestroy(Activity a) {
        disposed = true;
    }


    public void checkForEvent(){
        if(mDetails.events.size() <= mEpisodeEventIndex) return;
        if(mPlayer.getCurrentPosition() >= mDetails.events.get(mEpisodeEventIndex).timestamp){
            handleEpisodeEvent(mEpisodeEventIndex);
            mEpisodeEventIndex++;
        }
    }

    private boolean handleEpisodeEvent(int eventIndex){
        EpisodeEvent event = mDetails.events.get(eventIndex);
        EpisodeEvent nextEvent = null;
        if( mDetails.events.size() > eventIndex+1)
            nextEvent = mDetails.events.get(eventIndex+1);



        switch (event.type){
            case Skipped:
                if(nextEvent != null){
                    if(!mPlayer.canSeekForward()) Log.e(TAG, "cannot seek");
                    mPlayer.seekTo(nextEvent.timestamp);
                }
                break;
            case EpisodeStart:
                break;
            case Categories:
                break;
            case QuestAsked:
                Log.w(TAG, "should pause at " + event.timestamp);
                Log.w(TAG, "paused at " + mPlayer.getCurrentPosition());
                mPlayer.pause();
                mPlayer.seekTo(event.timestamp);
                mListener.onQuestionAsked();
                break;
            case AnswerRead:
                mListener.onAnswerRead(((AnswerReadEvent)event).questionInfo);
                break;
        }

        return true;
    }


}
