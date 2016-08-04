package com.seismicgames.jeopardyprototype.buzzer.transport;

import com.seismicgames.jeopardyprototype.Constants;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.client.DefaultWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.util.List;

/**
 * Created by jduffy on 6/27/16.
 */
public abstract class PingingWebSockClient extends WebSocketClient {

    public PingingWebSockClient(URI serverURI) {
        super(serverURI);
        setWebSocketFactory(new KeepAliveWebSocketClientFactory(this));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    }


    private static class KeepAliveWebSocketClientFactory extends DefaultWebSocketClientFactory{

        public KeepAliveWebSocketClientFactory(WebSocketClient webSocketClient) {
            super(webSocketClient);
        }

        @Override
        public WebSocket createWebSocket(WebSocketAdapter a, Draft d, Socket s) {
            initSocket(s);
            return super.createWebSocket(a, d, s);
        }

        @Override
        public WebSocket createWebSocket(WebSocketAdapter a, List<Draft> d, Socket s) {
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
