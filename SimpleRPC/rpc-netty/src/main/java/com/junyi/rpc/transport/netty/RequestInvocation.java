package com.junyi.rpc.transport.netty;

import com.junyi.rpc.transport.RequestHandler;
import com.junyi.rpc.transport.RequestHandlerRegistry;
import com.junyi.rpc.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(RequestInvocation.class);
    private final RequestHandlerRegistry requestHandlerRegistry;

    public RequestInvocation(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        RequestHandler handler = requestHandlerRegistry.get(command.getHeader().getType());
        if (null != handler) {
            Command response = handler.handle(command);
            if (null != response) {
                channelHandlerContext.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture -> {
                   if (!channelFuture.isSuccess()) {
                       logger.warn("write response fail", channelFuture.cause());
                       channelHandlerContext.channel().close();
                   }
                });
            } else {
                logger.info("Response if null");
            }
        } else {
            throw new Exception(String.format("No handler for request type : %d", command.getHeader().getType()));
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("Exception:", cause);
        super.exceptionCaught(ctx,cause);
        Channel channel = ctx.channel();
        if (channel.isActive())
            ctx.close();
    }
}
