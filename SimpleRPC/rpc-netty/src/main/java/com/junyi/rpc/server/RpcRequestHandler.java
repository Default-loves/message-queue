package com.junyi.rpc.server;

import com.junyi.rpc.client.ServiceTypes;
import com.junyi.rpc.client.stubs.RpcRequest;
import com.junyi.rpc.serialize.SerializeSupport;
import com.junyi.rpc.transport.RequestHandler;
import com.junyi.rpc.transport.command.Code;
import com.junyi.rpc.transport.command.Command;
import com.junyi.rpc.transport.command.Header;
import com.junyi.rpc.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class RpcRequestHandler implements ServiceProviderRegistry, RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    private static final Map<String, Object> serviceProviders = new HashMap<>();
    @Override
    public <T> void addServiceProvider(Class<T> serviceClass, T service) {
        serviceProviders.put(serviceClass.getCanonicalName(), service);
        logger.info("Add service: {}, provider: {}",
                serviceClass.getCanonicalName(), service.getClass().getCanonicalName());
    }

    @Override
    public Command handle(Command request) {
        Header header = request.getHeader();
        RpcRequest rpcRequest = SerializeSupport.parse(request.getPayload());
        try {
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (null != serviceProvider) {
                String arg = SerializeSupport.parse(rpcRequest.getSerializedArgs());
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), String.class);
                String result = (String) method.invoke(serviceProvider, arg);
                return new Command(new ResponseHeader(header.getRequestID(), header.getVersion(), type()), SerializeSupport.serialize(result));
            } else {
                logger.warn("No service provider {}#{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
                return new Command(new ResponseHeader(header.getRequestID(), header.getVersion(), header.getType(), Code.NO_PROVIDER.getCode(), "No provide"), new byte[0]);
            }
        } catch(Throwable r) {
            logger.warn("Exception: ", r);
            return new Command(new ResponseHeader(header.getRequestID(), header.getVersion(), header.getType(), Code.UNKNOWN_ERROR.getCode(), r.getMessage()), new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }
}
