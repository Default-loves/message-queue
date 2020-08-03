package com.junyi.rpc.transport;

public interface TransportServer {
    void start(RequestHandlerRegistry registry, int port) throws Exception;
    void stop();
}
