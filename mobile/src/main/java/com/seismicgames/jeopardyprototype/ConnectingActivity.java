package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.seismicgames.jeopardyprototype.controller.ControllerClient;
import com.seismicgames.jeopardyprototype.controller.HostScanner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConnectingActivity extends Activity {

    private static final String TAG = ConnectingActivity.class.getName();

    @BindView(R.id.fullscreen_content)
    protected TextView textView;

    private ControllerClient.HostScanTask task;

    private HostScanner hostScanner;

    private Handler mHandler = new Handler();
    private final CheckConnectivityRunnable CheckConnectivityRunnable = new CheckConnectivityRunnable();

    private ControllerClient client;


    private SpeechRecognizer speechRecognizer;

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.d(TAG, "onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
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
    protected void onButtonClick(){
//        if(client!= null){
//            if(client.getConnection().isOpen()) {
//                client.send("click@" + System.currentTimeMillis());
//            }else {
//                scanForHost();
//            }
//        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizer.startListening(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();



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

        if(client != null){
            client.close();
            client = null;
        }
        super.onStop();
    }

    private void scanForHost(){
        textView.setText("searching");
        hostScanner.scanForHost(this);
        mHandler.post(CheckConnectivityRunnable);
    }


    private class CheckConnectivityRunnable implements Runnable {

        @Override
        public void run() {
            if(client != null && !client.getConnection().isOpen()){
                client.close();
                client = null;
                scanForHost();
                return;
            } else if(hostScanner.client != null && hostScanner.client.getConnection().isOpen()){
                client = hostScanner.client;
                textView.setText("connected");
            }
            mHandler.postDelayed(this, 1000);
        }
    }

}
