package com.seismicgames.jeopardyprototype;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Enumeration;

/**
 * Created by jduffy on 7/19/16.
 */
public class InetAddressUtil {
    public static byte[] getWifiIp() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
             en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            if (intf.isLoopback()) {
                continue;
            }
            if (intf.isVirtual()) {
                continue;
            }
            if (!intf.isUp()) {
                continue;
            }
            if (intf.isPointToPoint()) {
                continue;
            }
            if (intf.getHardwareAddress() == null) {
                continue;
            }
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                 enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (inetAddress.getAddress().length == 4) {
                    return inetAddress.getAddress();
                }
            }
        }
        return null;
    }

    public static String getWifiHostAddress() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
             en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            if (intf.isLoopback()) {
                continue;
            }
            if (intf.isVirtual()) {
                continue;
            }
            if (!intf.isUp()) {
                continue;
            }
            if (intf.isPointToPoint()) {
                continue;
            }
            if (intf.getHardwareAddress() == null) {
                continue;
            }
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                 enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (inetAddress.getAddress().length == 4) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return null;
    }
}
