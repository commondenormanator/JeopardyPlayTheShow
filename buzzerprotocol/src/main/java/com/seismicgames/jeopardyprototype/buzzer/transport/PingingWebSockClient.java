package com.seismicgames.jeopardyprototype.buzzer.transport;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by jduffy on 6/27/16.
 */
public abstract class PingingWebSockClient extends WebSocketClient {


    private final PingThread pingThread = new PingThread();
    private PingRunnable ping;

    public PingingWebSockClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        ping = new PingRunnable(getConnection());
        pingThread.start();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        pingThread.finish();
    }


    private class PingThread extends Thread {
        private volatile boolean isFinished;

        public void finish() {
            isFinished = true;
        }

        @Override
        public void run() {
            while (!isFinished) {
                ping.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
