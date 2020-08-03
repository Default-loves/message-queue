package com.junyi.server;

import com.junyi.rpc.NameService;
import com.junyi.rpc.RpcAccessPoint;
import com.junyi.rpc.hello.HelloService;
import com.junyi.rpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    public static void main(String[] args) throws Exception {
        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        HelloService helloService = new HelloServiceImpl();
        logger.info("创建并启动RpcAccessPoint...");
        try(RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
            Closeable ignored = rpcAccessPoint.startService()) {
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            logger.info("put service {} to RPC server", serviceName);
            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
            logger.info("Register service {} in NameServer", serviceName);
            nameService.registerService(serviceName, uri);
            logger.info("I'm working, enter any key to exit");
            System.in.read();
            logger.info("Bye");
        }
    }
}
