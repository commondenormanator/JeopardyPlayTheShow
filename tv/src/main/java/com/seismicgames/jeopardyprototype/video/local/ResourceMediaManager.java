package com.seismicgames.jeopardyprototype.video.local;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.seismicgames.jeopardyprototype.R;
import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.video.MediaPlayerControlMediaManager;

/**
 * Created by jduffy on 7/12/16.
 */
public class ResourceMediaManager extends MediaPlayerControlMediaManager {

    private VideoView videoView;

    public static ResourceMediaManager getInstance(Context activity, ViewGroup videoContainer, EpisodeDetails episodeDetails) {
        VideoView videoView = new VideoView(activity);
        videoView.setZOrderOnTop(false);
        videoContainer.addView(videoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        Uri video = Uri.parse("android.resource://" + activity.getPackageName() + "/"
//                + R.raw.video); //do not add any extension

//        videoView.setVideoURI(Uri.withAppendedPath(Uri.fromFile(activity.getExternalFilesDir(null)), "JEOP6974_720p.mp4"));
        videoView.setVideoURI(Uri.withAppendedPath(Uri.fromFile(activity.getExternalFilesDir(null)), "JEOP6974_RIP.mp4"));


        return new ResourceMediaManager(videoView, episodeDetails);
    }

    private ResourceMediaManager(VideoView videoView, EpisodeDetails episodeDetails) {
        super(videoView, episodeDetails);
        this.videoView = videoView;
        videoView.setOnCompletionListener(new MediaCompleteListener());
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
