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
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seismicgames.jeopardyprototype.controller.ControllerServer;
import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.gameplay.GameState;
import com.seismicgames.jeopardyprototype.gameplay.engine.GameEngine;
import com.seismicgames.jeopardyprototype.gameplay.events.EpisodeGameObject;
import com.seismicgames.jeopardyprototype.video.MpxMediaManager;
import com.seismicgames.jeopardyprototype.video.MpxVideoPlayer;
import com.theplatform.adk.Player;
import com.theplatform.adk.player.event.api.data.MediaEndEvent;
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
public class GameActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    private Player player;

    private GameState gameState;
    private ControllerServer controllerServer;

    private EpisodeDetails episodeDetails = new EpisodeDetails();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adk_player);



        final ViewGroup videoContainer = (ViewGroup) this.findViewById(R.id.videoContainer);
        player = new Player(
                // inject player container view into ADK Player
                videoContainer
        );


        gameState = new GameState();



        controllerServer = new ControllerServer(){

            @Override
            public void onMessage(WebSocket conn, final String message) {
                super.onMessage(conn, message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(GameActivity.this, message, Toast.LENGTH_SHORT).show();


                    }
                });

            }
        };

        gameState.init(controllerServer, new MpxMediaManager(player, episodeDetails));



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
        gameState.onResume(this);
        gameState.startGame(episodeDetails);
    }

    @Override
    protected void onPause() {
        gameState.onPause(this);
        super.onPause();
    }




}
