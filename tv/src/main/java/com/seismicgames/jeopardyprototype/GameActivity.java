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
import android.view.ViewGroup;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerServer;
import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.gameplay.GameState;
import com.seismicgames.jeopardyprototype.ui.GameUiManager;
import com.seismicgames.jeopardyprototype.video.MpxMediaManager;
import com.theplatform.adk.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*
 * GameActivity class that loads MainFragment
 */
public class GameActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    private Player player;

    private GameState gameState;

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


        BuzzerServer server = new BuzzerServer();

        gameState.init(server, new MpxMediaManager(player, episodeDetails), new GameUiManager(this));

        server.start();


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

    }

    @Override
    protected void onResume() {
        super.onResume();
        player.getLifecycle().onResume();
        gameState.onResume(this);
        gameState.startGame(episodeDetails);
    }

    @Override
    protected void onPause() {
        gameState.onPause(this);
        player.getLifecycle().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        player.getLifecycle().destroy();
        super.onDestroy();
    }
}
