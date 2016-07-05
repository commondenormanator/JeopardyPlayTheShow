package com.seismicgames.jeopardyprototype.gameplay;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeEvent;
import com.seismicgames.jeopardyprototype.video.VideoPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jduffy on 6/29/16.
 */
public class EpisodeRunner implements Runnable {


    public final VideoPlayer mPlayer;

    private Handler handler;

    private List<EpisodeEvent> eventList = new ArrayList<>();

    private int nextEventIndex = 0;

    private volatile boolean finished;

    public EpisodeRunner(VideoPlayer mPlayer) {
        this.mPlayer = mPlayer;
        handler = new Handler(Looper.getMainLooper());
        eventList.add(new EpisodeEvent(10000, EpisodeEvent.Type.QuestAsked));
        eventList.add(new EpisodeEvent(20000, EpisodeEvent.Type.QuestAsked));
    }


    public void start(){
        new Thread(this).start();
    }


    private void handleEvent(EpisodeEvent event){
        handler.post(new EventHandlerRunnable(event));
    }


    @Override
    public void run() {

        while(!finished){

            Log.d("GAMELOOP", "LOOP");

            if(nextEventIndex >= eventList.size()){
                finished = true;
                break;
            }

            EpisodeEvent event = eventList.get(nextEventIndex);
            if(event.timestamp <= mPlayer.getCurrentPosition()){
                handleEvent(event);
                nextEventIndex++;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }



    private class EventHandlerRunnable implements Runnable{

        private final EpisodeEvent event;

        private EventHandlerRunnable(EpisodeEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            mPlayer.pause();


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPlayer.play();
                }
            }, 5000);
        }
    }


}
