package com.junyi.rpc.transport.netty;

import com.junyi.rpc.transport.InFlightRequest;
import com.junyi.rpc.transport.ResponseFuture;
import com.junyi.rpc.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: JY
 * Date: 2020/5/5 0005
 * Description:
 */
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseInvocation.class);
    private InFlightRequest inFlightRequest;

    public ResponseInvocation(InFlightRequest inFlightRequest) {
        this.inFlightRequest = inFlightRequest;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        ResponseFuture response = inFlightRequest.remove(command.getHeader().getRequestID());
        if (null != response) {
            response.getFuture().complete(command);
        } else {
            logger.info("Drop response:", response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("Exception:", cause);
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive())
            ctx.close();
    }
}
