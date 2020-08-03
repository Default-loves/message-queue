package com.junyi.rpc.transport;

import com.junyi.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class RequestHandlerRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerRegistry.class);
    private Map<Integer, RequestHandler> handlerMap = new HashMap<>();
    private static RequestHandlerRegistry instance;
    public static RequestHandlerRegistry getInstance() {
        if (null == instance) {
            instance = new RequestHandlerRegistry();
        }
        return instance;
    }

    public RequestHandlerRegistry() {
        Collection<RequestHandler> requestHandlers = ServiceSupport.loadAll(RequestHandler.class);
        for (RequestHandler handler : requestHandlers) {
            handlerMap.put(handler.type(), handler);
            logger.info("Load request handler, type: {}, class: {}", handler.type(), handler.getClass().getCanonicalName());
        }
    }
    public RequestHandler get(int type) {
        return handlerMap.get(type);
    }
}
