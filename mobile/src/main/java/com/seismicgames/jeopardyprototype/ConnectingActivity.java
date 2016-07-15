package com.seismicgames.jeopardyprototype;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerClient;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzerMessageClientListener;
import com.seismicgames.jeopardyprototype.controller.ControllerClient;
import com.seismicgames.jeopardyprototype.buzzer.HostScanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConnectingActivity extends Activity {

    private static final String TAG = ConnectingActivity.class.getName();

    @BindView(R.id.fullscreen_content)

    protected TextView textView;
    @BindView(R.id.dummy_button)
    protected Button buzzerButton;

    @BindView(R.id.buzzer_outline)
    protected ImageView pulseImage;
    protected AnimatorSet pulseAnim;

    private ControllerClient.HostScanTask task;

    private HostScanner hostScanner;

    private Handler mHandler = new Handler();
    private final CheckConnectivityRunnable CheckConnectivityRunnable = new CheckConnectivityRunnable();

    private final Runnable stopSpeechRunnable = new Runnable() {
        @Override
        public void run() {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
        }
    };

    private BuzzerClient client;


    private SpeechRecognizer speechRecognizer;

    private RecognitionListener recognitionListener = new RecognitionListener() {

        List<String> lastResults = new ArrayList<>();

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.d(TAG, "onReadyForSpeech");
            setButtonPulseAnimate(true);
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
            lastResults.clear();
        }

        @Override
        public void onRmsChanged(float v) {
            Log.d(TAG, "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.d(TAG, "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech");
            setButtonPulseAnimate(false);
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
                List<String> resCopy = new ArrayList<String>(results);
                resCopy.removeAll(lastResults);
                if(resCopy.size() > 0){
                    client.sendAnswerRequest(results);
                    lastResults = results;
                }
            }

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
                    client.sendAnswerRequest(results);
                    lastResults = results;
                }
            }
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            Log.d(TAG, "onEvent" + i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connecting_screen);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.dummy_button)
    protected void onButtonClick() {
        if (client != null && client.getConnection().isOpen()) {
            client.sendBuzzInRequest();
        }
    }

    @OnClick(R.id.restartButton)
    protected void onRestartClick() {
        if (client != null && client.getConnection().isOpen()) {
            client.sendRestartRequest();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=  PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            }
        }


        hostScanner = new HostScanner();
        scanForHost();

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this.recognitionListener);
    }

    @Override
    protected void onStop() {
        if(speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }

        hostScanner.stop();

        if(client != null){
            client.close();
            client = null;
        }
        super.onStop();
    }

    private void scanForHost(){
        textView.setText("searching");
        hostScanner.scanForHost(this);
        setButtonEnabled(false);
        mHandler.post(CheckConnectivityRunnable);
    }

    private void setClientConnection(BuzzerClient client){
        setButtonEnabled(true);
        this.client = client;
        textView.setText("connected");
        this.client.setMessageListener(new BuzzerMessageClientListener() {
            @Override
            public void onBuzzInResponse(BuzzInResponse response) {
                if(response.buzzInValid) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.removeCallbacks(stopSpeechRunnable);
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
                            }
                            speechRecognizer.startListening(intent);
                            mHandler.postDelayed(stopSpeechRunnable, Constants.AnswerTimeout - 1000);
                        }
                    });
                }
            }
        });
    }

    private void setButtonEnabled(boolean isEnabled){
        if (isEnabled) {
            buzzerButton.setEnabled(true);
            pulseImage.setVisibility(View.VISIBLE);
        }
        else {
            setButtonPulseAnimate(false);
            buzzerButton.setEnabled(false);
            pulseImage.setVisibility(View.INVISIBLE);
        }
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

    private class CheckConnectivityRunnable implements Runnable {

        @Override
        public void run() {
            if(client != null && !client.getConnection().isOpen()){
                client.close();
                client = null;
                scanForHost();
                return;
            } else if(client == null && hostScanner.client != null && hostScanner.client.getConnection().isOpen()){
                setClientConnection(hostScanner.client);
            }
            mHandler.postDelayed(this, 1000);
        }
    }

}
