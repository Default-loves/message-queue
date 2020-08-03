package com.junyi.client;

import com.junyi.rpc.NameService;
import com.junyi.rpc.RpcAccessPoint;
import com.junyi.rpc.hello.HelloService;
import com.junyi.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description: 客户端调用 RPC 框架
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String[] args) throws IOException {
        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        String name = "Master MQ";
        try(RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            URI uri = nameService.lookupService(serviceName);
            logger.info("Find service {}, uri: {}", serviceName, uri);
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
            logger.info("Request service");
            String result = helloService.hello(name);
            logger.info("Response : {}", result);
        }
    }
}
