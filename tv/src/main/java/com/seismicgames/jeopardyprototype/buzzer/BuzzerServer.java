package com.seismicgames.jeopardyprototype.buzzer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.seismicgames.jeopardyprototype.buzzer.message.AnswerRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzInResponse;
import com.seismicgames.jeopardyprototype.buzzer.message.BuzzerMessage;
import com.seismicgames.jeopardyprototype.buzzer.message.RestartRequest;
import com.seismicgames.jeopardyprototype.buzzer.message.VoiceCaptureState;
import com.seismicgames.jeopardyprototype.buzzer.transport.PingingWebSockServer;
import com.seismicgames.jeopardyprototype.gameplay.GameState;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jduffy on 6/27/16.
 */
public class BuzzerServer extends PingingWebSockServer implements GameState.BuzzerManager {

    private static final String TAG = BuzzerServer.class.getName();

    private GameState.BuzzerEventListener listener;

    public static final int PORT = 8989;

    private Gson gson = new Gson();


    public BuzzerServer() {
        super(new InetSocketAddress(PORT));
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

        JsonObject json = gson.fromJson(message, JsonObject.class);

        switch ( json.get("type").getAsString()){
            case "BuzzInRequest":
                listener.onUserBuzzIn();
                break;
            case "AnswerRequest":
                listener.onUserAnswer(gson.fromJson(json, AnswerRequest.class));
                break;
            case "RestartRequest":
                listener.onUserRestart();
                break;
            case "VoiceCaptureState":
                listener.onVoiceCaptureState(gson.fromJson(json, VoiceCaptureState.class));
                break;
            default:
                Log.e(TAG, "Unhandled message: " + message);
        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void registerListener(GameState.BuzzerEventListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isBuzzerConnected() {
        return this.connections().size() > 0;
    }


    @Override
    public void sendBuzzInResponse(boolean isValidBuzz) {
        for (WebSocket conn: connections()) {
            conn.send(gson.toJson(new BuzzInResponse(isValidBuzz)));
        }
    }
}
