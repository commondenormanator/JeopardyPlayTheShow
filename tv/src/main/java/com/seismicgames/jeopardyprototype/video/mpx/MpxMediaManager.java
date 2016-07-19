package com.seismicgames.jeopardyprototype.video.mpx;

import android.app.Activity;
import android.view.ViewGroup;

import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.video.MediaPlayerControlMediaManager;
import com.theplatform.adk.Player;
import com.theplatform.adk.player.event.api.data.MediaEndEvent;
import com.theplatform.adk.player.event.api.data.PlayerEventListener;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jduffy on 7/13/16.
 */
public class MpxMediaManager extends MediaPlayerControlMediaManager {

    private Player mPlayer;

    public static MpxMediaManager getInstance(ViewGroup videoContainer, EpisodeDetails details) {
        Player player = new Player(videoContainer);
        return new MpxMediaManager(player, details);
    }

    public MpxMediaManager(Player player, EpisodeDetails details) {
        super(player.asMediaPlayerControl(), details);
        mPlayer = player;

        try {
            mPlayer.loadReleaseUrl(new URL("http://link.theplatform.com/s/spe/media/MXha0q_JklY_"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mPlayer.asEventDispatcher().addEventListener(MediaEndEvent.getType(), new MediaCompleteListener());
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
