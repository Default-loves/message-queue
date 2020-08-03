package com.junyi.rpc.transport;

import com.junyi.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public interface Transport {
    /**
     *  发送请求的命令
     * @param request 请求命令
     * @return Future
     */
    CompletableFuture<Command> send(Command request);
}
