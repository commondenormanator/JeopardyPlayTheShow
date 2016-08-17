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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;
import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.episode.EpisodeParser;
import com.seismicgames.jeopardyprototype.gameplay.GameState;
import com.seismicgames.jeopardyprototype.ui.GameUiManager;
import com.seismicgames.jeopardyprototype.util.file.ExternalFileUtil;
import com.seismicgames.jeopardyprototype.video.local.ResourceMediaManager;
import com.seismicgames.jeopardyprototype.video.mpx.MpxMediaManager;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * GameActivity class that loads MainFragment
 */
@BuzzerScene(BuzzerScene.Scene.BUZZER)
public class GameActivity extends BuzzerActivity {
    /**
     * Called when the activity is first created.
     */
    private static final String EPISODE_PATH_EXTRA = "EPISODE_PATH_EXTRA";
    private static final String EPISODE_STREAMING_EXTRA = "EPISODE_STREAMING_EXTRA";

    private GameState gameState;

    private File episodeDir;
    private boolean useStreaming;
    private EpisodeDetails episodeDetails;

    @BindView(R.id.videoContainer)
    ViewGroup videoContainer;

    public static void show(Activity context, File episodePath, boolean streaming){
        Intent i = new Intent(context, GameActivity.class);
        i.putExtra(EPISODE_PATH_EXTRA, episodePath);
        i.putExtra(EPISODE_STREAMING_EXTRA, streaming);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adk_player);
        ButterKnife.bind(this);
        gameState = new GameState();

        episodeDir = (File) getIntent().getSerializableExtra(EPISODE_PATH_EXTRA);
        useStreaming = getIntent().getBooleanExtra(EPISODE_STREAMING_EXTRA, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameState.onResume(this);
        new LoadEpisodeAsyncTask().execute();
    }

    @Override
    protected void onPause() {
        gameState.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        gameState.onDestroy(this);
        super.onDestroy();
    }

    private void onEpisodeLoaded() {

        if (!isFinishing()) {
            if(!gameState.isInitialized()) {
                if(useStreaming){
                    gameState.init(episodeDetails, BuzzerConnectionManager.getInstance(getApplication()), MpxMediaManager.getInstance(videoContainer, episodeDir), new GameUiManager(this));
                }else {
                    gameState.init(episodeDetails, BuzzerConnectionManager.getInstance(getApplication()), ResourceMediaManager.getInstance(this, videoContainer, episodeDir), new GameUiManager(this));
                    //                gameState.init(BuzzerConnectionManager.getInstance(getApplication()), TextureViewMediaManager.getInstance(this, videoContainer, episodeDetails), new GameUiManager(this));
                }

            }

            gameState.startGame(episodeDetails);
        }
    }


    private class LoadEpisodeAsyncTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(episodeDetails != null) return true;

            InputStream game = null;
            InputStream meta = null;
            InputStream marker = null;
            try{
                File videoFile = new File(episodeDir, "video.mp4");
                if(!videoFile.exists()) return false;
                File gameFile = new File(episodeDir, "game.csv");
                if(gameFile == null || !gameFile.exists()) return false;
                File metaFile = new File(episodeDir, "meta.csv");
                if(metaFile == null || !metaFile.exists()) return false;

                game = new FileInputStream(gameFile);
                meta = new FileInputStream(metaFile);

                String markerString;
                File markerFile = new File(episodeDir, "marker.csv");

                if(markerFile == null || !markerFile.exists()) {
                    markerString = "";
                }else {
                    marker = new FileInputStream(markerFile);
                    markerString = IOUtils.toString(marker);
                }

                episodeDetails = EpisodeParser.parse(IOUtils.toString(game), IOUtils.toString(meta), markerString);

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(game);
                IOUtils.closeQuietly(meta);
                IOUtils.closeQuietly(marker);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if(success) {
                onEpisodeLoaded();
            }else {
                Toast.makeText(GameActivity.this, "Could not load episode", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}
