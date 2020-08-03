package com.junyi.rpc;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * 注册中心
 */
public interface NameService {
    //支持的协议
    Collection<String> supportSchema();
    //连接注册中心
    void connect(URI nameServiceURI);
    //注册服务
    void registerService(String serviceName, URI uri) throws IOException;
    //查询服务
    URI lookupService(String serviceName) throws IOException;

}
