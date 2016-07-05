import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by jduffy on 6/29/16.
 */
public abstract class BuzzerServer implements Closeable{

    public static final int PORT = 8989;

    private WebSocketServer mSocketServer;



    public void close() throws IOException {
        try {
            mSocketServer.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    protected abstract void log(String s);
    protected abstract void logWarn(String s);
    protected abstract void logError(String s);


    private class BuzzerSocketServer extends WebSocketServer {
        public BuzzerSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            log(String.format("onOpen %s <-> %s", conn.getLocalSocketAddress(), conn.getRemoteSocketAddress()));
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
           log(String.format("onClose %s <-> %s %s %s", conn.getLocalSocketAddress(), conn.getRemoteSocketAddress(), code, reason));
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            log(String.format("onMessage %s", message));
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            logError(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
