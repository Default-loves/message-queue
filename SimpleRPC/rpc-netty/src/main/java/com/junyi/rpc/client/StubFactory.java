package com.junyi.rpc.client;

import com.junyi.rpc.transport.Transport;

public interface StubFactory {
    <T> T createStub(Transport transport, Class<T> service);
}
