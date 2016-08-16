package com.seismicgames.jeopardyprototype;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerScene;
import com.seismicgames.jeopardyprototype.buzzer.client.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.GameplayEventListener;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.EpisodeMarkerList;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@BuzzerScene(BuzzerScene.Scene.BUZZER)
public class GameBuzzerActivity extends ConnectedActivity {

    private static final String TAG = GameBuzzerActivity.class.getName();

    @BindView(R.id.fullscreen_content)

    protected TextView textView;
    @BindView(R.id.dummy_button)
    protected Button buzzerButton;

    @BindView(R.id.buzzer_outline)
    protected ImageView pulseImage;
    protected AnimatorSet pulseAnim;

    private AlertDialog dialog;
    private Handler mHandler = new Handler();

    private final Runnable stopSpeechRunnable = new Runnable() {
        @Override
        public void run() {
            speechRecognizer.stopListening();
        }
    };

    private BuzzerConnectionManager mConnection;

    private SpeechRecognizer speechRecognizer;

    private RecognitionListener recognitionListener = new RecognitionListener() {

        List<String> lastResults = new ArrayList<>();

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.d(TAG, "onReadyForSpeech");
            setButtonPulseAnimate(true);
            mConnection.gameplaySender().sendVoiceState(VoiceCaptureState.State.LISTENING);
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
            lastResults.clear();
            mConnection.gameplaySender().sendVoiceState(VoiceCaptureState.State.SPEECH_BEGIN);
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.d(TAG, "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech");
            setButtonPulseAnimate(false);
            mConnection.gameplaySender().sendVoiceState(VoiceCaptureState.State.SPEECH_END);
        }

        @Override
        public void onError(int i) {
            Log.d(TAG, "onError" + i);
            setButtonPulseAnimate(false);
        }

        @Override
        public void onResults(Bundle bundle) {
            Log.d(TAG, "onResults");
            List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if(results != null && results.size() > 0){
                textView.setText(results.get(0));
                for(String s : results) {
                    Log.d(TAG, s);
                }

                //check if answers are new
                List<String> resCopy = new ArrayList<>(results);
                resCopy.removeAll(lastResults);
                if(resCopy.size() > 0){
                    mConnection.gameplaySender().sendAnswerRequest(results);
                    lastResults = results;
                }
            }

            mConnection.gameplaySender().sendVoiceState(VoiceCaptureState.State.RECOGNITION_COMPLETE);
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            Log.d(TAG, "onPartialResults");
            List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if(results != null && results.size() > 0){
                textView.setText(results.get(0));
                for(String s : results) {
                    Log.d(TAG, s);
                }

                //check if answers are new
                List<String> resCopy = new ArrayList<String>(results);
                resCopy.removeAll(lastResults);
                if(resCopy.size() > 0){
                    mConnection.gameplaySender().sendAnswerRequest(results);
                    lastResults = results;
                }
            }
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            Log.d(TAG, "onEvent" + i);
        }
    };

    private GameplayEventListener gameplayEventListener = new GameplayEventListener(){
        @Override
        public void onBuzzInResponse(BuzzInResponse response) {
            if(response.buzzInValid) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(speechRecognizer != null) {

                            mHandler.removeCallbacks(stopSpeechRunnable);
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false);
                            }
                            speechRecognizer.setRecognitionListener(recognitionListener);
                            speechRecognizer.startListening(intent);
                            mHandler.postDelayed(stopSpeechRunnable, Constants.AnswerListenTimeout);
                        }
                    }
                });
            }
        }

        @Override
        public void onStopVoiceRec() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(speechRecognizer != null) {
                        speechRecognizer.cancel();
                        speechRecognizer.setRecognitionListener(null); //maybe this resets it?
                    }
                }
            });
        }

        @Override
        public void onEpisodeMarkers(EpisodeMarkerList markers) {
            mEpisodeMarkers = markers;
            invalidateOptionsMenu();
        }

    };

    private EpisodeMarkerList mEpisodeMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connecting_screen);
        ButterKnife.bind(this);
        mConnection = BuzzerConnectionManager.getInstance(getApplication());

        mConnection.addListener(gameplayEventListener);

        mConnection.gameplaySender().sendMarkerRequest();

        setButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("Pause")
                .setIcon(android.R.drawable.ic_media_pause)
                .setOnMenuItemClickListener(
                        new MenuItem.OnMenuItemClickListener() {

                            private boolean pause = true;

                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                mConnection.gameplaySender().sendPauseGame(pause);
                                pause = !pause;
                                menuItem.setIcon(pause ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
                                return true;
                            }
                        }
                ).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        for (int i = 0; mEpisodeMarkers != null && i < mEpisodeMarkers.markers.size(); i++) {
            final int index = i;
            menu.add("Jump To: " + mEpisodeMarkers.markers.get(i))
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            mConnection.gameplaySender().sendUserJumpToMarker(index);
                            return true;
                        }
                    });
        }
        return true;
    }



    @OnClick(R.id.dummy_button)
    protected void onButtonClick() {
        mConnection.gameplaySender().sendBuzzInRequest();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=  PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            }
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this.recognitionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onStop() {
        if(speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        mHandler.removeCallbacks(stopSpeechRunnable);

        super.onStop();
    }

    private void setButtonEnabled(boolean isEnabled) {
        if (isEnabled) {
            buzzerButton.setEnabled(true);
            pulseImage.setVisibility(View.VISIBLE);
        } else {
            setButtonPulseAnimate(false);
            buzzerButton.setEnabled(false);
            pulseImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        mConnection.removeListener(gameplayEventListener);
        super.onDestroy();
    }

    private void setButtonPulseAnimate(boolean animate){
        if(animate) {
            if (pulseAnim == null) {
                pulseAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.pulse);
                pulseAnim.setTarget(pulseImage);
            }
            if(!pulseAnim.isRunning()) pulseAnim.start();
        } else {
            if (pulseAnim != null && pulseAnim.isRunning()){
                pulseAnim.end();
            }
        }
    }

    private DialogInterface.OnClickListener onQuitEpisode(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mConnection.gameplaySender().sendQuitGame();
            }
        };
    }

    @Override
    public void onBackPressed() {
        if(mConnection.isBuzzerConnected()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialog = dialogBuilder
                .setMessage("Are you sure you want to quit this episode?")
                .setPositiveButton(android.R.string.ok, onQuitEpisode())
                .setCancelable(true)
                .show();
        } else {
            super.onBackPressed();
        }
    }



}
