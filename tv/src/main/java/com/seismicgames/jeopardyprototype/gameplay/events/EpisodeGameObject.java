package com.seismicgames.jeopardyprototype.gameplay.events;

import android.widget.Toast;

import com.seismicgames.jeopardyprototype.gameplay.EpisodeRunner;
import com.seismicgames.jeopardyprototype.gameplay.engine.GameEngine;
import com.seismicgames.jeopardyprototype.gameplay.engine.GameObject;
import com.seismicgames.jeopardyprototype.video.VideoPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jduffy on 6/29/16.
 */
public class EpisodeGameObject extends GameObject {

    public final VideoPlayer mPlayer;

    private List<EpisodeEvent> eventList = new ArrayList<>();
    private int nextEventIndex = 0;


    public EpisodeGameObject(GameEngine engine, VideoPlayer mPlayer) {
        super(engine);
        this.mPlayer = mPlayer;
        eventList.add(new EpisodeEvent(180000, EpisodeEvent.Type.Categories));
        eventList.add(new EpisodeEvent(220000, EpisodeEvent.Type.QuestAsked));
    }

    @Override
    public void pump() {
        if(nextEventIndex >= eventList.size()){
            engine.removeGameObject(this);
            return;
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


    private void handleEvent(EpisodeEvent event){
        final String toast = event.type.toString();
        engine.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(engine.getCurrentActivity(), toast, Toast.LENGTH_SHORT).show();
            }
        });

        if(event.type == EpisodeEvent.Type.QuestAsked){

        }


    }



}
