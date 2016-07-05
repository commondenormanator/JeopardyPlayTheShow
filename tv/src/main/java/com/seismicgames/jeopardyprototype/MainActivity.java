/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seismicgames.jeopardyprototype.controller.ControllerServer;
import com.seismicgames.jeopardyprototype.gameplay.EpisodeRunner;
import com.seismicgames.jeopardyprototype.gameplay.engine.GameEngine;
import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeGameObject;
import com.seismicgames.jeopardyprototype.video.MpxVideoPlayer;
import com.theplatform.adk.Player;
import com.theplatform.adk.player.event.api.ListenerRegistration;
import com.theplatform.adk.player.event.api.data.MediaEndEvent;
import com.theplatform.adk.player.event.api.data.MediaPlayingEvent;
import com.theplatform.adk.player.event.api.data.MediaStartEvent;
import com.theplatform.adk.player.event.api.data.PlayerEventListener;

import org.java_websocket.WebSocket;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private GameEngine engine = new GameEngine();

    private Player player;

    private ControllerServer controllerServer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adk_player);



        final ViewGroup videoContainer = (ViewGroup) this.findViewById(R.id.videoContainer);
        player = new Player(
                // inject player container view into ADK Player
                videoContainer
        );

        player.asEventDispatcher().addEventListener(MediaEndEvent.getType(), new MediaEndEventListener());
        playShow();


        engine.addGameObject(new EpisodeGameObject(engine, new MpxVideoPlayer(player)));


        controllerServer = new ControllerServer(){

            @Override
            public void onMessage(WebSocket conn, final String message) {
                super.onMessage(conn, message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                        togglePause();
                    }
                });

            }
        };



        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int ip = wm.getConnectionInfo().getIpAddress();

        ByteBuffer buffer = ByteBuffer.allocate((4));
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(ip);
        try {
            Log.e("ADDRESS", "listening on ip: " + InetAddress.getByAddress(buffer.array()).getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        controllerServer.start();



    }

    @Override
    protected void onResume() {
        super.onResume();
        engine.start();
        engine.registerActivity(this);
    }

    @Override
    protected void onPause() {
        engine.unregisterActivity(this);
        engine.stop();
        super.onPause();
    }

    public void togglePause(){
        if(player.asMediaPlayerControl().isPlaying()){
            player.asMediaPlayerControl().pause();
            Log.e("player", "pause");
        }else{
            player.asMediaPlayerControl().start();
            Log.e("player", "start");
        }
    }

    public void playShow(){
        try {
            player.playReleaseUrl(new URL("http://link.theplatform.com/s/spe/media/MXha0q_JklY_"));
            player.asMediaPlayerControl().pause();

            player.asEventDispatcher().addEventListener(MediaStartEvent.getType(),
                    new PlayerEventListener<MediaStartEvent>() {
                        private boolean isDone;
                        @Override
                        public void onPlayerEvent(MediaStartEvent event) {
                            if(!isDone) {
                                isDone = true;
                                player.asMediaPlayerControl().seekTo(115000);
                            }

                        }
                    });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BUTTON_A){

            togglePause();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private class MediaEndEventListener implements PlayerEventListener<MediaEndEvent>
    {
        @Override
        public void onPlayerEvent(MediaEndEvent event)
        {
            playShow();
        }
    };

}
