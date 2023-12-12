package com.example.netty.training;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpMessage) {
            var request = (FullHttpMessage) msg;
            var response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set("Content-Type", "text/plain");
            response.content().writeCharSequence("Hello World", StandardCharsets.UTF_8);


            ctx.writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
        }
    }
}
