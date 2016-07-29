package com.seismicgames.jeopardyprototype.buzzer.client;

import android.util.Log;

import com.google.gson.Gson;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzerMessageClientListener;
import com.seismicgames.jeopardyprototype.buzzer.transport.PingingWebSockClient;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by jduffy on 6/27/16.
 */
public class BuzzerClient extends PingingWebSockClient {

    private static final String TAG = BuzzerClient.class.getName();

    public static final int SERVER_PORT = 8989;

    private Gson gson = new Gson();

    public BuzzerClient(URI serverURI) {
        super(serverURI);
    }

    private BuzzerMessageClientListener mListener;
    public void setMessageListener(BuzzerMessageClientListener mListener) {
        this.mListener = mListener;
    }
    private BuzzerClientListener mConnListener;
    public void setConnectionListener(BuzzerClientListener connListener) {
        this.mConnListener = connListener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        super.onOpen(handshakedata);
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, message);

        if(mConnListener != null) mConnListener.onMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        super.onClose(code, reason, remote);
        if(mConnListener != null) mConnListener.onConnectionClosed();
    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, ""+ex.getMessage());
        ex.printStackTrace();
    }

}
