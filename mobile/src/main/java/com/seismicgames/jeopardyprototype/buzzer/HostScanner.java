package com.seismicgames.jeopardyprototype.buzzer;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;

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

public class HostScanner {

    private static final String TAG = HostScanner.class.getName();
    private static final int POOL_SIZE = 16;

    private ExecutorService poolExecutor;

    private volatile byte byteCounter = 0;
    private List<ScanRunnable> list = new ArrayList<>();

    public BuzzerClient client;

    private synchronized byte getNextByte() {
        return byteCounter++;
    }

    public void scanForHost(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ip = wm.getConnectionInfo().getIpAddress();

        ByteBuffer buffer = ByteBuffer.allocate((4));
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(ip);
        byte[] localHost = buffer.array();

        poolExecutor = Executors.newFixedThreadPool(POOL_SIZE);


        for (int i = 0; i < POOL_SIZE; i++) {
            ScanRunnable runnable = new ScanRunnable(this, localHost);
            list.add(runnable);
            poolExecutor.submit(runnable);
        }
    }

    public void stop(){
        finishScan(this.client);
        if(this.client != null){
            this.client.close();
        }
    }

    @WorkerThread
    static BuzzerClient attemptConnect(byte[] address) {
        try {
            InetAddress possibleHost = InetAddress.getByAddress(address);
            Log.e(TAG, String.format("Attempting: %s", possibleHost.getHostAddress()));
            if (possibleHost.isReachable(1000)) {
                Log.d(TAG, String.format("%s is reachable", possibleHost.getHostAddress()));

                BuzzerClient client = new BuzzerClient(new URI("ws", "", possibleHost.getHostAddress(), BuzzerClient.SERVER_PORT, "", "", ""));
                try {
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (client.getConnection().isOpen()) {
                    return client;
                }
                client.close();

            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void finishScan(BuzzerClient client) {
        for (ScanRunnable r : list) {
            r.canceled = true;
        }

        if(poolExecutor != null) {
            poolExecutor.shutdown();
            poolExecutor = null;
        }
        this.client = client;
    }

    private static class ScanRunnable implements Runnable {
        private final HostScanner hostScanner;
        private final byte[] subnet;
        public volatile boolean canceled = false;
        public BuzzerClient client;

        private ScanRunnable(HostScanner hostScanner, byte[] subnet) {
            this.hostScanner = hostScanner;
            this.subnet = subnet;
        }

        @Override
        public void run() {
            while (!canceled) {
                client = HostScanner.attemptConnect(new byte[]{
                        subnet[0],
                        subnet[1],
                        subnet[2],
                        hostScanner.getNextByte()
                });
                if (client != null) {
                    hostScanner.finishScan(client);
                }
            }
        }
    }

}

