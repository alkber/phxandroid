package org.phxandroid.net.servers;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class AddrUtil {
    public static String asId(SocketAddress addr) {
        if (addr instanceof InetSocketAddress) {
            InetSocketAddress iaddr = (InetSocketAddress) addr;
            return iaddr.getAddress().getHostAddress() + ":" + iaddr.getPort();
        }

        return addr.toString();
    }
}
