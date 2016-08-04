package com.seismicgames.jeopardyprototype.buzzer.transport;


import com.seismicgames.jeopardyprototype.Constants;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.DefaultWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
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

    public PingingWebSockServer(InetSocketAddress address) {
        super(address);
        setWebSocketFactory(new KeepAliveWebSocketClientFactory());
    }

    public PingingWebSockServer(InetSocketAddress address, int decoders) {
        super(address, decoders);
        setWebSocketFactory(new KeepAliveWebSocketClientFactory());
    }

    public PingingWebSockServer(InetSocketAddress address, List<Draft> drafts) {
        super(address, drafts);
        setWebSocketFactory(new KeepAliveWebSocketClientFactory());
    }

    public PingingWebSockServer(InetSocketAddress address, int decodercount, List<Draft> drafts) {
        super(address, decodercount, drafts);
        setWebSocketFactory(new KeepAliveWebSocketClientFactory());
    }

    public PingingWebSockServer(InetSocketAddress address, int decodercount, List<Draft> drafts, Collection<WebSocket> connectionscontainer) {
        super(address, decodercount, drafts, connectionscontainer);
        setWebSocketFactory(new KeepAliveWebSocketClientFactory());
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop(int timeout) throws IOException, InterruptedException {
        super.stop(timeout);
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        super.stop();
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    }

    private static class KeepAliveWebSocketClientFactory extends DefaultWebSocketServerFactory {

        @Override
        public WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d, Socket s) {
            initSocket(s);
            return super.createWebSocket(a, d, s);
        }

        @Override
        public WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> d, Socket s) {
            initSocket(s);
            return super.createWebSocket(a, d, s);
        }

        private void initSocket(Socket s){
            try {
                s.setSoTimeout(Constants.SocketTimeout);
                s.setKeepAlive(true);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

}
