package com.seismicgames.jeopardyprototype.buzzer.transport;


import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jduffy on 6/29/16.
 */
public abstract class PingingWebSockServer extends WebSocketServer {

    private final PingThread pingThread = new PingThread();

    private Map<WebSocket, PingRunnable> pings = new HashMap<>();

    public PingingWebSockServer() throws UnknownHostException {
    }

    public PingingWebSockServer(InetSocketAddress address) {
        super(address);
    }

    public PingingWebSockServer(InetSocketAddress address, int decoders) {
        super(address, decoders);
    }

    public PingingWebSockServer(InetSocketAddress address, List<Draft> drafts) {
        super(address, drafts);
    }

    public PingingWebSockServer(InetSocketAddress address, int decodercount, List<Draft> drafts) {
        super(address, decodercount, drafts);
    }

    public PingingWebSockServer(InetSocketAddress address, int decodercount, List<Draft> drafts, Collection<WebSocket> connectionscontainer) {
        super(address, decodercount, drafts, connectionscontainer);
    }

    @Override
    public void start() {
        super.start();
        pingThread.start();
    }

    @Override
    public void stop(int timeout) throws IOException, InterruptedException {
        pingThread.finish();
        super.stop(timeout);
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        pingThread.finish();
        super.stop();
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        pings.put(conn, new PingRunnable(conn));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        pings.remove(conn);
    }

    private class PingThread extends Thread {
        private volatile boolean isFinished;

        public void finish() {
            isFinished = true;
        }

        @Override
        public void run() {
            while (!isFinished) {
                for (PingRunnable ping :
                        new ArrayList<>(pings.values())) {
                    ping.run();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
