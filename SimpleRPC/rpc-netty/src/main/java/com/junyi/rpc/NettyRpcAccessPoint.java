package com.junyi.rpc;

import com.junyi.rpc.client.StubFactory;
import com.junyi.rpc.server.ServiceProviderRegistry;
import com.junyi.rpc.spi.ServiceSupport;
import com.junyi.rpc.transport.RequestHandlerRegistry;
import com.junyi.rpc.transport.Transport;
import com.junyi.rpc.transport.TransportClient;
import com.junyi.rpc.transport.TransportServer;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {
    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);
    private TransportServer server;
    private TransportClient client = ServiceSupport.load(TransportClient.class);
    private Map<URI, Transport> clientMap = new HashMap<>();
    private StubFactory stubFactory = ServiceSupport.load(StubFactory.class);
    private ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);
    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 30000L);
        } catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return uri;
    }

    @Override
    public synchronized Closeable startService() throws Exception {
        if (null == server) {
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(), port);
        }
        return () -> {
            if(server != null) {
                server.stop();
            }
        };
    }
    @Override
    public void close() {
        if(null != server) {
            server.stop();
        }
        client.close();
    }
}
