package com.junyi.rpc.transport.netty;

import com.junyi.rpc.transport.InFlightRequest;
import com.junyi.rpc.transport.ResponseFuture;
import com.junyi.rpc.transport.Transport;
import com.junyi.rpc.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class NettyTransport implements Transport {
    private Channel channel;
    private InFlightRequest inFlightRequest;

    public NettyTransport(Channel channel, InFlightRequest inFlightRequest) {
        this.channel = channel;
        this.inFlightRequest = inFlightRequest;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            inFlightRequest.put(new ResponseFuture(request.getHeader().getRequestID(), completableFuture));
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable throwable) {
            inFlightRequest.remove(request.getHeader().getRequestID());
            completableFuture.completeExceptionally(throwable);
        }
        return completableFuture;
    }
}
