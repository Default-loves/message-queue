package com.junyi.rpc.transport;

import io.netty.util.Timeout;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

public interface TransportClient extends Closeable {
    Transport createTransport(SocketAddress address, long connectionTimeout) throws TimeoutException, InterruptedException;
    @Override
    void close();
}
