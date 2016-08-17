package com.seismicgames.jeopardyprototype.video.mpx;

import android.app.Activity;
import android.view.ViewGroup;

import com.seismicgames.jeopardyprototype.util.file.ExternalFileUtil;
import com.seismicgames.jeopardyprototype.video.MediaPlayerControlMediaManager;
import com.theplatform.adk.Player;
import com.theplatform.adk.player.event.api.data.MediaEndEvent;
import com.theplatform.adk.player.event.api.data.PlayerEventListener;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jduffy on 7/13/16.
 */
public class MpxMediaManager extends MediaPlayerControlMediaManager {

    private Player mPlayer;

    private URL url;
    private boolean playbackStarted = false;

    public static MpxMediaManager getInstance(ViewGroup videoContainer, File episodeDir) {
        Player player = new Player(videoContainer);

        File videoUrlFile = ExternalFileUtil.getFile(videoContainer.getContext(), new File(episodeDir, ExternalFileUtil.VideoUrlFileName).getPath());

        String url = null;
        if(videoUrlFile.exists()){
            try {
                url = FileUtils.readFileToString(videoUrlFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new MpxMediaManager(player, url);
    }

    public MpxMediaManager(Player player, String url) {
        super(player.asMediaPlayerControl());
        mPlayer = player;

        try {
            this.url = new URL(url);
            mPlayer.loadReleaseUrl(this.url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mPlayer.asEventDispatcher().addEventListener(MediaEndEvent.getType(), new MediaCompleteListener());
    }

    @Override
    public void play() {
        if(!playbackStarted) {
            mPlayer.playReleaseUrl(url);
            playbackStarted = true;
        }
        super.play();
    }

    @Override
    public void onActivityResume(Activity a) {
        super.onActivityResume(a);
        mPlayer.getLifecycle().onResume();
    }

    @Override
    public void onActivityPause(Activity a) {
        super.onActivityPause(a);
        mPlayer.getLifecycle().onPause();
    }

    @Override
    public void onActivityDestroy(Activity a) {
        super.onActivityDestroy(a);
        mPlayer.getLifecycle().destroy();
    }

    private class MediaCompleteListener implements PlayerEventListener<MediaEndEvent> {
        @Override
        public void onPlayerEvent(MediaEndEvent mediaEndEvent) {
            if (mListener != null) mListener.onMediaComplete();
        }
    }

}
