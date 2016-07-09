package com.seismicgames.jeopardyprototype.video;

import android.util.Log;

import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.gameplay.GameState;
import com.seismicgames.jeopardyprototype.gameplay.events.AnswerReadEvent;
import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeEvent;
import com.theplatform.adk.Player;
import com.theplatform.adk.playback.normalizer.api.SeekSuccessEvent;
import com.theplatform.adk.player.event.api.data.MediaEndEvent;
import com.theplatform.adk.player.event.api.data.MediaPlayingEvent;
import com.theplatform.adk.player.event.api.data.MediaSeekCompleteEvent;
import com.theplatform.adk.player.event.api.data.PlayerEventListener;
import com.theplatform.adk.player.event.api.data.ReleaseStartEvent;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jduffy on 7/1/16.
 */
public class MpxMediaManager implements GameState.MediaManager {

    private static final String TAG = MpxMediaManager.class.getName();

    private Player mPlayer;
    private EpisodeDetails mDetails;

    private GameState.MediaEventListener mListener;

    private int mEpisodeEventIndex;

    private MediaSeekCompleteEventListener mediaSeekCompleteEventListener = new MediaSeekCompleteEventListener();


    private Thread thread =  new Thread(){
        @Override
        public void run() {
            while(true){

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


    public MpxMediaManager(Player player, EpisodeDetails details) {
        this.mPlayer = player;
        this.mDetails = details;

        mPlayer.asEventDispatcher().addEventListener(MediaEndEvent.getType(), new MediaEndEventListener());
        mPlayer.asEventDispatcher().addEventListener(MediaSeekCompleteEvent.getType(), mediaSeekCompleteEventListener);
        //mPlayer.asEventDispatcher().addEventListener(MediaPlayingEvent.getType(), new MediaPlayingEventListener());

        thread.start();


        try {
            mPlayer.loadReleaseUrl(new URL("http://link.theplatform.com/s/spe/media/MXha0q_JklY_"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void registerListener(GameState.MediaEventListener listener) {
        mListener = listener;
    }


    @Override
    public void play() {
        shouldPlay = true;
        mPlayer.asMediaPlayerControl().start();

    }

    @Override
    public void pause() {
        mPlayer.asMediaPlayerControl().pause();

    }


    public void checkForEvent(){
        if(mDetails.events.size() <= mEpisodeEventIndex) return;
        if(mPlayer.asMediaPlayerControl().getCurrentPosition() >= mDetails.events.get(mEpisodeEventIndex).timestamp){
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
                    if(!mPlayer.asMediaPlayerControl().canSeekForward()) Log.e(TAG, "cannot seek");
                    mPlayer.asMediaPlayerControl().seekTo(nextEvent.timestamp);
                }
                break;
            case EpisodeStart:
                break;
            case Categories:
                break;
            case QuestAsked:
                Log.w(TAG, "should pause at " + event.timestamp);
                Log.w(TAG, "paused at " + mPlayer.asMediaPlayerControl().getCurrentPosition());
                mPlayer.asMediaPlayerControl().pause();
                mediaSeekCompleteEventListener.lastSeek = event.timestamp;
                mPlayer.asMediaPlayerControl().seekTo(event.timestamp);
                mListener.onQuestionAsked();
                break;
            case AnswerRead:
                mListener.onAnswerRead(((AnswerReadEvent)event).questionInfo);
                break;
        }

        return true;
    }

    private class MediaEndEventListener implements PlayerEventListener<MediaEndEvent>
    {
        @Override
        public void onPlayerEvent(MediaEndEvent event)
        {
//            mPlayer.asMediaPlayerControl().start();
        }
    }
    private class MediaSeekCompleteEventListener implements PlayerEventListener<MediaSeekCompleteEvent>
    {
        private int lastSeek;

        @Override
        public void onPlayerEvent(MediaSeekCompleteEvent event)
        {
            Log.w(TAG, String.format("Desired : %d Seek: %d -> %d.  Actual %d",
                    lastSeek,
                    event.getSeekInfo().getStart().getCurrentTime(),
                    event.getSeekInfo().getEnd().getCurrentTime(),
                    mPlayer.asMediaPlayerControl().getCurrentPosition()));
        }
    }
    private class MediaPlayingEventListener implements PlayerEventListener<MediaPlayingEvent>
    {
        @Override
        public void onPlayerEvent(MediaPlayingEvent event)
        {
            if(event.getTimeInfo().getCurrentTime() >= mDetails.events.get(mEpisodeEventIndex).timestamp){
                handleEpisodeEvent(mEpisodeEventIndex);
                mEpisodeEventIndex++;
            }
        }
    }
}
