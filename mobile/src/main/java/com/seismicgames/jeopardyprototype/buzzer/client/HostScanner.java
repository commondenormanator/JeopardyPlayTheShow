package com.seismicgames.jeopardyprototype.buzzer.client;

import android.support.annotation.WorkerThread;
import android.util.Log;

import com.seismicgames.jeopardyprototype.InetAddressUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
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

    //public BuzzerClient client;

    private Listener mListener;


    public interface Listener {
        void onHostFound(BuzzerClient client);
    }

    private boolean isScanning = false;


    private synchronized byte getNextByte() {
        return byteCounter++;
    }

    public boolean scanForHost(Listener listener) {
        finishScan();
        isScanning = true;
        mListener = listener;
        byte[] localHost = null;
        try {
            localHost = InetAddressUtil.getWifiIp();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if (localHost == null) {
            return false;
        }

        poolExecutor = Executors.newFixedThreadPool(POOL_SIZE);


        for (int i = 0; i < POOL_SIZE; i++) {
            ScanRunnable runnable = new ScanRunnable(this, localHost);
            list.add(runnable);
            poolExecutor.submit(runnable);
        }

        return true;
    }

    public void stop() {
        finishScan();
    }

    public boolean isScanning() {
        return isScanning;
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

    private void finishScan() {
        isScanning = false;
        for (ScanRunnable r : list) {
            r.canceled = true;
        }
        list.clear();

        if (poolExecutor != null) {
            poolExecutor.shutdown();
            poolExecutor = null;
        }
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
                byte lastIpField = hostScanner.getNextByte();
                client = HostScanner.attemptConnect(new byte[]{
                        subnet[0],
                        subnet[1],
                        subnet[2],
                        lastIpField
                });
                if (client != null) {
                    try {
                        hostScanner.mListener.onHostFound(client);
                    } finally {
                        hostScanner.finishScan();
                        //set byte counter to right ip.  Aids reconnect speed.
                        hostScanner.byteCounter = lastIpField;
                    }
                }
            }
        }
    }

}

