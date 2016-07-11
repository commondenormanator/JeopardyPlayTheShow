package com.seismicgames.jeopardyprototype;

import android.Manifest;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private ControllerClient.HostScanTask task;

    private HostScanner hostScanner;

    private Handler mHandler = new Handler();
    private final CheckConnectivityRunnable CheckConnectivityRunnable = new CheckConnectivityRunnable();

    private final Runnable stopSpeechRunnable = new Runnable() {
        @Override
        public void run() {
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
        }

        @Override
        public void onError(int i) {
            Log.d(TAG, "onError" + i);
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
        buzzerButton.setEnabled(false);
        mHandler.post(CheckConnectivityRunnable);
    }

    private void setClientConnection(BuzzerClient client){
        buzzerButton.setEnabled(true);
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
                            mHandler.postDelayed(stopSpeechRunnable, 9000);
                        }
                    });
                }
            }
        });
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
