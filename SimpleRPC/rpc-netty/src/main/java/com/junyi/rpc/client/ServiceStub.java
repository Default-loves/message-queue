package com.junyi.rpc.client;

import com.junyi.rpc.transport.Transport;

public interface ServiceStub {
    void setTransport(Transport transport);
}
