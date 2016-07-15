package com.seismicgames.jeopardyprototype.buzzer;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzerMessageClientListener;
import com.seismicgames.jeopardyprototype.buzzer.message.RestartRequest;
import com.seismicgames.jeopardyprototype.buzzer.transport.PingingWebSockClient;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

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

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        super.onOpen(handshakedata);
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, message);

        JsonObject json = gson.fromJson(message, JsonObject.class);

        switch ( json.get("type").getAsString()){
            case "BuzzInResponse":
                onBuzzInResponse(gson.fromJson(json, BuzzInResponse.class));
                break;
            default:
                Log.e(TAG, "Unhandled message: " + message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        super.onClose(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, ex.getMessage());
        ex.printStackTrace();
    }

    public void sendBuzzInRequest(){
        send(gson.toJson(new BuzzInRequest()));
    }

    public void sendAnswerRequest(List<String> answers){
        send(gson.toJson(new AnswerRequest(answers)));
    }


    public void onBuzzInResponse(BuzzInResponse response){
        if(mListener != null) mListener.onBuzzInResponse(response);
    }

    public void sendRestartRequest(){
        send(gson.toJson(new RestartRequest()));
    }

}
