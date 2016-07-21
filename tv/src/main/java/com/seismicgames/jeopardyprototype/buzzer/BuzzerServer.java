package com.seismicgames.jeopardyprototype.buzzer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seismicgames.jeopardyprototype.InetAddressUtil;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.buzzer.transport.PingingWebSockServer;
import com.seismicgames.jeopardyprototype.gameplay.GameState;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jduffy on 6/27/16.
 */
public class BuzzerServer extends PingingWebSockServer {

    private static final String TAG = BuzzerServer.class.getName();

    private BuzzerServerListener listener;

    public static final int PORT = 8989;


    public BuzzerServer() {
        super(new InetSocketAddress(PORT));
    }

    @Override
    public void start() {
        super.start();
        try {
            Log.e(TAG, "listening on ip: " + InetAddressUtil.getWifiHostAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws IOException, InterruptedException {
        super.stop();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        super.onOpen(conn, handshake);
        Log.d(TAG, String.format("onOpen %s <-> %s", conn.getLocalSocketAddress(), conn.getRemoteSocketAddress()));

        if (listener != null)
            listener.onBuzzerConnectivityChange(true);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        super.onClose(conn, code, reason, remote);
        Log.d(TAG, String.format("onClose %s %s <-> %s", reason, conn.getLocalSocketAddress(), conn.getRemoteSocketAddress()));


        if (listener != null)
            listener.onBuzzerConnectivityChange(false);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG, message);

        if (listener != null)
            listener.onMessage(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }


    public void registerListener(BuzzerServerListener listener) {
        this.listener = listener;
    }


    public void unregisterListener(BuzzerServerListener listener) {
        this.listener = null;
    }


    public boolean isBuzzerConnected() {
        return this.connections().size() > 0;
    }


    public void sendMessage(String message) {
        for (WebSocket conn: connections()) {
            conn.send(message);
        }
    }

}
