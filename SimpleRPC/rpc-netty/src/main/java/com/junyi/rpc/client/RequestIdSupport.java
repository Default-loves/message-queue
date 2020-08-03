package com.junyi.rpc.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class RequestIdSupport {
    private static final AtomicInteger nextRequestId = new AtomicInteger(0);
    public static int next() {
        return nextRequestId.getAndIncrement();
    }
}
