package com.seismicgames.jeopardyprototype.video.local;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.seismicgames.jeopardyprototype.util.file.ExternalFileUtil;
import com.seismicgames.jeopardyprototype.video.MediaPlayerControlMediaManager;

import java.io.File;

/**
 * Created by jduffy on 7/12/16.
 */
public class ResourceMediaManager extends MediaPlayerControlMediaManager {

    private VideoView videoView;

    public static ResourceMediaManager getInstance(Context activity, ViewGroup videoContainer, File episodeDir) {
        VideoView videoView = new VideoView(activity);
        videoView.setZOrderOnTop(false);
        videoContainer.addView(videoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        File video = new File(episodeDir, "video.mp4");

        videoView.setVideoURI(Uri.fromFile(video));
        return new ResourceMediaManager(videoView);
    }

    private ResourceMediaManager(VideoView videoView) {
        super(videoView);
        this.videoView = videoView;
        videoView.setOnCompletionListener(new MediaCompleteListener());
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                Log.v("OnInfoListener", "what " + what + " extra " + extra);
                return false;
            }
        });
    }

    @Override
    public void onActivityDestroy(Activity a) {
        super.onActivityDestroy(a);
        videoView.stopPlayback();
    }

    private class MediaCompleteListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if(mListener != null) mListener.onMediaComplete();
        }
    }

}
