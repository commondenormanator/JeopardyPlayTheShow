package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.seismicgames.jeopardyprototype.buzzer.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.listeners.ConnectionEventListener;

public class BuzzerDisconnectedActivity extends Activity implements ConnectionEventListener {

    public static void show(Context uiContext){
        uiContext.startActivity(new Intent(uiContext, BuzzerDisconnectedActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzzer_disconnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BuzzerConnectionManager.getInstance(getApplication()).addListener(this);
        if(BuzzerConnectionManager.getInstance(getApplication()).isBuzzerConnected()){
            finish();
        }
    }

    @Override
    protected void onPause() {
        BuzzerConnectionManager.getInstance(getApplication()).removeListener(this);
        super.onPause();
    }

    @Override
    public void onBuzzerConnectivityChange(boolean isConnected) {
        if(isConnected){
            finish();
        }
    }
}
