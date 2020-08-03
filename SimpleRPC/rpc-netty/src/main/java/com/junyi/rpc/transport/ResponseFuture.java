package com.junyi.rpc.transport;

import com.junyi.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class ResponseFuture {
    private int requestId;
    private CompletableFuture<Command> future;
    private long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        this.timestamp = System.nanoTime();
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
