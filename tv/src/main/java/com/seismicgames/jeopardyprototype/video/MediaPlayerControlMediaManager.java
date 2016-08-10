package com.seismicgames.jeopardyprototype.video;

import android.app.Activity;
import android.util.Log;
import android.widget.MediaController;

import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.gameplay.GameState;
import com.seismicgames.jeopardyprototype.gameplay.events.AnswerReadEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.QuestionAskedEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.WagerEvent;

/**
 * Created by jduffy on 7/1/16.
 */
public class MediaPlayerControlMediaManager implements GameState.MediaManager {

    private static final String TAG = MediaPlayerControlMediaManager.class.getName();

    private MediaController.MediaPlayerControl mPlayer;
    private EpisodeDetails mDetails;

    protected GameState.MediaEventListener mListener;

    private int mEpisodeEventIndex;

    private boolean disposed;

    private int seekOnResume = 0;

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


    public MediaPlayerControlMediaManager(MediaController.MediaPlayerControl player) {
        this.mPlayer = player;
    }

    @Override
    public void setEpisodeDetails(EpisodeDetails details) {
        this.mDetails = details;
    }

    @Override
    public void registerListener(GameState.MediaEventListener listener) {
        mListener = listener;
    }


    @Override
    public void play() {
        if(mDetails == null) throw new IllegalStateException("details must be set");
        if(thread.getState() == Thread.State.NEW) thread.start();

        shouldPlay = true;
        mPlayer.start();
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void reset() {
        mPlayer.pause();
        mPlayer.seekTo(0);
        seekOnResume = 0;
        mEpisodeEventIndex = 0;
    }

    @Override
    public void seekTo(int timestamp) {
        mPlayer.seekTo(timestamp);
        mEpisodeEventIndex = mEpisodeEventIndex >= mDetails.events.size() || mDetails.events.get(mEpisodeEventIndex).timestamp > timestamp ? 0 : mEpisodeEventIndex;

        //search for correct episode index.  No loop body is intentional
        for (;mEpisodeEventIndex < mDetails.events.size()
                     && mDetails.events.get(mEpisodeEventIndex).timestamp < timestamp;
             mEpisodeEventIndex++);

    }

    @Override
    public void onActivityResume(Activity a) {
        mPlayer.seekTo(seekOnResume);
    }

    @Override
    public void onActivityPause(Activity a) {
        if(mPlayer.getCurrentPosition() != 0) {
            seekOnResume = mPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onActivityDestroy(Activity a) {
        disposed = true;
    }


    public void checkForEvent(){
        if(!mPlayer.isPlaying()) return;
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
            case CommercialStart:
                if(nextEvent != null){
                    if(!mPlayer.canSeekForward()) Log.e(TAG, "cannot seek");
                    mPlayer.seekTo(nextEvent.timestamp);
                }
                break;
            case EpisodeStart:
                break;
            case EpisodeEnd:
                mListener.onMediaComplete();
                break;
            case Categories:
                break;
            case QuestAsked:
                Log.w(TAG, "should pause at " + event.timestamp);
                Log.w(TAG, "paused at " + mPlayer.getCurrentPosition());
                if(mListener.onQuestionAsked((QuestionAskedEvent) event)) {
                    mPlayer.pause();
                    Log.w(TAG, "lag was " + (mPlayer.getCurrentPosition() - event.timestamp));
                }
                break;
            case AnswerRead:
                mListener.onAnswerRead((AnswerReadEvent)event);
                break;
            case Wager:
                mPlayer.pause();
                mListener.onWager((WagerEvent) event);
                break;
            case HomePlayerIntro:
                mPlayer.pause();
                mListener.onHomePlayerIntro();
                break;
        }

        return true;
    }


}
