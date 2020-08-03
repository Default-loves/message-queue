package com.junyi.rpc.transport.netty;

import com.junyi.rpc.transport.InFlightRequest;
import com.junyi.rpc.transport.Transport;
import com.junyi.rpc.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class NettyClient implements TransportClient {
    private EventLoopGroup ioEventGroup;
    private Bootstrap bootstrap;
    private final InFlightRequest inFlightRequests;
    private List<Channel> channels = new LinkedList<>();

    public NettyClient() {
        inFlightRequests = new InFlightRequest();
    }
    @Override
    public Transport createTransport(SocketAddress address, long connectionTimeout) throws TimeoutException, InterruptedException {
        return new NettyTransport(createChannel(address, connectionTimeout), inFlightRequests);
    }

    private synchronized Channel createChannel(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null!");
        }
        if (ioEventGroup == null) {
            ioEventGroup = newIoEventGroup();
        }
        if (bootstrap == null){
            ChannelHandler channelHandlerPipeline = newChannelHandlerPipeline();
            bootstrap = newBootstrap(channelHandlerPipeline, ioEventGroup);
        }
        ChannelFuture channelFuture;
        Channel channel;
        channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectionTimeout)) {
            throw new TimeoutException();
        }
        channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
        channels.add(channel);
        return channel;
    }
    private EventLoopGroup newIoEventGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }
    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast(new ResponseDecoder())
                        .addLast(new RequestEncoder())
                        .addLast(new ResponseInvocation(inFlightRequests));
            }
        };
    }
    private Bootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup ioEventGroup) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .group(ioEventGroup)
                .handler(channelHandler)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return bootstrap;
    }

    @Override
    public void close() {
        for (Channel channel : channels) {
            if (null != channel)
                channel.close();
        }
        if (ioEventGroup != null)
            ioEventGroup.shutdownGracefully();
        inFlightRequests.close();
    }
}
