package com.seismicgames.jeopardyprototype.video.local;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.seismicgames.jeopardyprototype.R;
import com.seismicgames.jeopardyprototype.episode.EpisodeDetails;
import com.seismicgames.jeopardyprototype.util.ExternalFileUtil;
import com.seismicgames.jeopardyprototype.video.MediaPlayerControlMediaManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by jduffy on 7/12/16.
 */
public class TextureViewMediaManager extends MediaPlayerControlMediaManager implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private MediaPlayer mMediaPlayer;

    public static TextureViewMediaManager getInstance(Context activity, ViewGroup videoContainer, EpisodeDetails episodeDetails) {
        TextureView textureView = new TextureView(activity);

        textureView.setBackgroundColor(textureView.getResources().getColor(R.color.question_blue));
        videoContainer.addView(textureView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        File video = ExternalFileUtil.getFile(activity, "video.mp4");

        MediaPlayer mediaPlayer= new MediaPlayer();
        try {
            mediaPlayer.setDataSource(activity, Uri.fromFile(video));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new TextureViewMediaManager(textureView, mediaPlayer);
    }

    private TextureViewMediaManager(TextureView textureView, MediaPlayer mediaPlayer) {
        super(new SimpleWrapper(mediaPlayer));
        this.mMediaPlayer = mediaPlayer;
        this.mTextureView = textureView;
        textureView.setSurfaceTextureListener(this);
        mediaPlayer.setOnCompletionListener(new MediaCompleteListener());
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
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
        mMediaPlayer.release();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mMediaPlayer.setSurface(new Surface(mTextureView.getSurfaceTexture()));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    private class MediaCompleteListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if(mListener != null) mListener.onMediaComplete();
        }
    }

    private static class SimpleWrapper implements MediaController.MediaPlayerControl{
        private final MediaPlayer mMediaPlayer;

        private SimpleWrapper(MediaPlayer mMediaPlayer) {
            this.mMediaPlayer = mMediaPlayer;
        }

        @Override
        public void start() {
            mMediaPlayer.start();
        }

        @Override
        public void pause() {
            mMediaPlayer.pause();
        }

        @Override
        public int getDuration() {
            return mMediaPlayer.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mMediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(int i) {
            mMediaPlayer.seekTo(i);
        }

        @Override
        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
        }

        @Override
        public int getBufferPercentage() {
            return 100;
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getAudioSessionId() {
            return mMediaPlayer.getAudioSessionId();
        }
    };

}
