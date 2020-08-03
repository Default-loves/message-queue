package com.junyi.rpc;

import com.junyi.rpc.spi.ServiceSupport;

import java.io.Closeable;
import java.net.URI;

/**
 * RPC对外提供的接口
 */
public interface RpcAccessPoint extends Closeable {
    //客户端获取服务实例
    <T> T getRemoteService(URI uri, Class<T> serviceClass);

    //服务端将服务实例添加到注册中心
    <T> URI addServiceProvider(T service, Class<T> serviceClass);

    Closeable startService() throws  Exception;

    /**
     * 获取注册中心的引用
     * @param nameServiceUri 注册中心的URI
     * @return 注册中心引用
     */
    default NameService getNameService(URI nameServiceUri) {
        for (NameService nameService : ServiceSupport.loadAll(NameService.class)) {
            if (nameService.supportSchema().contains(nameServiceUri.getScheme())) {
                nameService.connect(nameServiceUri);
                return nameService;
            }
        }
        return null;
    }
}
