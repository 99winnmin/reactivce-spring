package com.example.netty.eventLoop;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

@Slf4j
public class NettySeparatedEchoServer {
    // ByteBuf 로부터 CharSequence 를 읽어서 ctx.fireChannelRead 를 통해 다음 handler 에게 전달
    private static ChannelInboundHandler requestHandler() {
        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                if (msg instanceof ByteBuf) {
                    try {
                        var buf = (ByteBuf) msg;
                        var len = buf.readableBytes();
                        var charset = StandardCharsets.UTF_8;
                        var body = buf.readCharSequence(len, charset);
                        log.info("RequestHandler.channelRead: " + body);

                        ctx.fireChannelRead(body);
                    } finally {
                        // ByteBuf 를 릴리스하여 리소스 해제
                        ReferenceCountUtil.release(msg);
                    }
                }
            }
        };
    }

    // 이전 handler 로부터 String 을 받아서 buffer 를 생성해서 값을 집어넣고
    // ctx.write 를 통해 다음 handler 에게 전달
    private static ChannelOutboundHandler responseHandler() {
        return new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(
                ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {

                if (msg instanceof String) {
                    log.info("ResponseHandler.write: " + msg);
                    var body = (String) msg;
                    var charset = StandardCharsets.UTF_8;
                    var buf = ctx.alloc().buffer();
                    buf.writeCharSequence(body, charset);
                    ctx.write(buf, promise);
                }
            }
        };
    }

    // 이전 handler 로부터 String 타입의 msg 를 수신
    // 그대로 ctx.writeAndFlush 를 통해 다음 handler(prev - outboundhandler) 에게 전달
    private static ChannelInboundHandler echoHandler() {
        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                if (msg instanceof String) {
                    var request = (String) msg;
                    log.info("EchoHandler.channelRead: " + request);

                    ctx.writeAndFlush(request)
                        .addListener(ChannelFutureListener.CLOSE);
                }
            }
        };
    }

    private static ChannelInboundHandler acceptor(EventLoopGroup childGroup) {
        var executorGroup = new DefaultEventExecutorGroup(4);

        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                log.info("Acceptor.channelRead");
                if (msg instanceof SocketChannel) {
                    SocketChannel socketChannel = (SocketChannel) msg;
                    socketChannel.pipeline().addLast(
                        executorGroup, new LoggingHandler(LogLevel.INFO));
                    socketChannel.pipeline().addLast(
                        requestHandler(),
                        responseHandler(),
                        echoHandler()
                    );
                    childGroup.register(socketChannel);
                }
            }
        };
    }

    // 들어오는 ByteBuf 를 String 으로 바꿔서 전달하는 ChannelInboundHandler 기반의 requestHand/er 추가
    // 나가는 String 을 ByteBuf 로 바꿔서 전달하는 ChannelOutboundHandler 기반의 responseHandler 추가
    // But 매번 이렇게 많은 코드를 작성해야하나? -> Encoder/Decoder 를 통해 해결
    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup(4);

        NioServerSocketChannel serverSocketChannel = new NioServerSocketChannel();
        parentGroup.register(serverSocketChannel);
        serverSocketChannel.pipeline().addLast(acceptor(childGroup));

        serverSocketChannel.bind(new InetSocketAddress(8080))
            .addListener(future -> {
                if (future.isSuccess()) {
                    log.info("Server bound to port 8080");
                } else {
                    log.info("Failed to bind to port 8080");
                    parentGroup.shutdownGracefully();
                    childGroup.shutdownGracefully();
                }
            });
    }
}
