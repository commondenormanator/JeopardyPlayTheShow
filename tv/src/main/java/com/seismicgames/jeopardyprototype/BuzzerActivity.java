package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.os.Bundle;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.listeners.ConnectionEventListener;

/**
 * Created by jduffy on 7/19/16.
 */
public abstract class BuzzerActivity extends Activity {

    private boolean isVisible;
    private ConnectionEventListener mListener = new ConnectionEventListener() {
        @Override
        public void onBuzzerConnectivityChange(boolean isConnected) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(isVisible){
                        onDisconnect();
                    }
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        BuzzerConnectionManager.getInstance(getApplication()).addListener(mListener);
        if(!BuzzerConnectionManager.getInstance(getApplication()).isBuzzerConnected()){
            onDisconnect();
        }
    }

    @Override
    protected void onPause() {
        isVisible = false;
        BuzzerConnectionManager.getInstance(getApplication()).removeListener(mListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void onDisconnect(){
        BuzzerDisconnectedActivity.show(this);
    }

}