package com.junyi.server;

import com.junyi.rpc.hello.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(String name) {
        logger.info("HelloServiceImpl收到: {}.", name);
        String ret = "Hello, " + name;
        logger.info("HelloServiceImpl返回: {}.", ret);
        return ret;
    }
}
