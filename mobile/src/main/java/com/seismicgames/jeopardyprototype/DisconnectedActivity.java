package com.seismicgames.jeopardyprototype;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.seismicgames.jeopardyprototype.buzzer.client.BuzzerConnectionManager;
import com.seismicgames.jeopardyprototype.buzzer.client.listeners.ConnectionEventListener;

public class DisconnectedActivity extends Activity implements ConnectionEventListener {


    protected BuzzerConnectionManager mConnection;

    public static void show(Context uiContext) {
        uiContext.startActivity(new Intent(uiContext, DisconnectedActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnected);
        mConnection = BuzzerConnectionManager.getInstance(getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mConnection.isBuzzerConnected()) {
            showConnectActivity();
            return;
        }
        mConnection.addListener(this);

    }

    @Override
    protected void onPause() {
        mConnection.removeListener(this);
        super.onPause();
    }

    @Override
    public void onBuzzerConnectivityChange(boolean isConnected) {
        if (isConnected) {
            showConnectActivity();
        }
    }

    private void showConnectActivity() {
        finish();
        ConnectedActivity.changeSceneIfNeeded(this, mConnection.getScene());
    }
}
