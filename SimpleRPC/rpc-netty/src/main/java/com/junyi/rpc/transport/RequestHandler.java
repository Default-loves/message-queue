package com.junyi.rpc.transport;

import com.junyi.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 请求处理器
 */
public interface RequestHandler {
    /**
     * 请求处理
     * @param request
     * @return
     */
    Command handle(Command request);

    /**
     * 处理请求的类型
     * @return
     */
    int type();
}
