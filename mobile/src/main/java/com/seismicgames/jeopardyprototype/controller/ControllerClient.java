package com.seismicgames.jeopardyprototype.controller;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.WorkerThread;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jduffy on 6/27/16.
 */
public class ControllerClient extends WebSocketClient {

    private static final String TAG = ControllerClient.class.getName();

    public static final int SERVER_PORT = 8989;

    private LooperThread looperThread = new LooperThread();
    private PingRunnable ping;

    public ControllerClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        looperThread.start();
        ping = new PingRunnable(getConnection());
        looperThread.mHandler.post(ping);
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        looperThread.looper.quit();
    }

    @Override
    public void onError(Exception ex) {

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

    public static class HostScanTask extends AsyncTask<Void, Void, Void> {

        private final Context context;
        private byte[] localHost;

        public volatile boolean cancelled = false;

        public ControllerClient client;


        public HostScanTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            byte b = 0;
            while (!cancelled) {
                try {
                    InetAddress possibleHost = InetAddress.getByAddress(new byte[]{localHost[0], localHost[1], localHost[2], b});
                    Log.e(TAG, String.format("Attempting: %s", possibleHost.getHostAddress()));
                    if (possibleHost.isReachable(500)) {
                        Log.d(TAG, String.format("%s is reachable", possibleHost.getHostAddress()));

                        client = new ControllerClient(new URI("ws", "", possibleHost.getHostAddress(), SERVER_PORT, "", "", ""));
                        try {
                            client.connectBlocking();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(client.getConnection().isOpen()){
                            break;
                        }
                        client.close();
                        client = null;

                    }
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
                b++;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            int ip = wm.getConnectionInfo().getIpAddress();

            ByteBuffer buffer = ByteBuffer.allocate((4));
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(ip);
            localHost = buffer.array();
        }


        @Override
        protected void onPostExecute(Void obj) {
            super.onPostExecute(obj);

        }

    }



}
