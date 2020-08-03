package com.junyi.rpc.client.stubs;

import com.junyi.rpc.client.RequestIdSupport;
import com.junyi.rpc.client.ServiceStub;
import com.junyi.rpc.client.ServiceTypes;
import com.junyi.rpc.serialize.SerializeSupport;
import com.junyi.rpc.transport.Transport;
import com.junyi.rpc.transport.command.Code;
import com.junyi.rpc.transport.command.Command;
import com.junyi.rpc.transport.command.Header;
import com.junyi.rpc.transport.command.ResponseHeader;

import java.util.concurrent.ExecutionException;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public abstract class AbstractStub implements ServiceStub {
    private Transport transport;

    public byte[] invokeRemote(RpcRequest request) {
        Header header = new Header(RequestIdSupport.next(), 1, ServiceTypes.TYPE_RPC_REQUEST);
        byte[] payload = SerializeSupport.serialize(request);
        Command command = new Command(header, payload);
        try {
            Command responseCommand = transport.send(command).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable r) {
            throw new RuntimeException(r);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
