package com.example.netty.eventLoop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyShortEchoServer {
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

    @SneakyThrows
    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup(4);

        // BootStrap 객체는 netty 서버, 클라이언트를 쉽게 만들 수 있게 편의 메서드 재공
        var bootstrap = new ServerBootstrap();
        var executorGroup = new DefaultEventExecutorGroup(4);
        var stringEncoder = new StringEncoder();
        var stringDecoder = new StringDecoder();

        var bind = bootstrap
            .group(parentGroup, childGroup) // eventLoopGroup 을 등록, parentGroup 은 acceptor, childGroup 은 worker
            .channel(NioServerSocketChannel.class) // channel class 를 기반으로 instance 자동 생성
            .childHandler(new ChannelInitializer<SocketChannel>() { // connect 되었을 때 실행할 코드
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline()
                        .addLast(executorGroup, new LoggingHandler(LogLevel.INFO))
                        .addLast(stringEncoder, stringDecoder, echoHandler());
                }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .bind(8080);

        bind.sync().addListener(future -> { // 특정 호스트, 포트에 bind 하고 channelFuture 반환
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
