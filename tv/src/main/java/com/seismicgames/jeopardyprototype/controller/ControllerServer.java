package com.seismicgames.jeopardyprototype.controller;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
public class ControllerServer extends WebSocketServer implements GameState.BuzzerManager {

    private static final String TAG = ControllerServer.class.getName();

    private GameState.BuzzerEventListener listener;

    public static final int PORT = 8989;

    private LooperThread looperThread = new LooperThread();

    private Map<WebSocket, PingRunnable> pings = new HashMap<>();

    public ControllerServer() {
        super(new InetSocketAddress(PORT));
        looperThread.start();
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        looperThread.looper.quitSafely();
        super.stop();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG,String.format("onOpen %s <-> %s", conn.getLocalSocketAddress(), conn.getRemoteSocketAddress()));

        PingRunnable ping = new PingRunnable(conn);
        pings.put(conn, ping);
        looperThread.mHandler.post(ping);

        if(listener != null)
            listener.onBuzzerConnectivityChange(true);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG,String.format("onClose %s %s <-> %s", reason, conn.getLocalSocketAddress(), conn.getRemoteSocketAddress()));
        PingRunnable ping = pings.remove(conn);
        if(ping != null) {
            looperThread.mHandler.removeCallbacks(ping);
        }
        if(listener != null)
            listener.onBuzzerConnectivityChange(false);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG, message);
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


    class LooperThread extends Thread {
        public Handler mHandler;
        Looper looper;
        public void run() {
            Looper.prepare();

            mHandler = new Handler();

            looper = Looper.myLooper();
            Looper.loop();
        }
    }


    private class PingRunnable implements Runnable{

        private WebSocket conn;

        private PingRunnable(WebSocket conn) {
            this.conn = conn;
        }

        @Override
        public void run() {
            Log.d(TAG, "ping");
            FramedataImpl1 fd = new FramedataImpl1(Framedata.Opcode.PING);
            fd.setFin(true);
            conn.sendFrame(fd);
            looperThread.mHandler.postDelayed(this, 1000);
        }
    }

}
