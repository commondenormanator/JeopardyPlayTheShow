package com.seismicgames.jeopardyprototype.buzzer.transport;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;

public class PingRunnable implements Runnable {

    private WebSocket conn;

    public PingRunnable(WebSocket conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        if (conn.isOpen()) {
            FramedataImpl1 fd = new FramedataImpl1(Framedata.Opcode.PING);
            fd.setFin(true);
            conn.sendFrame(fd);
        }

    }
}