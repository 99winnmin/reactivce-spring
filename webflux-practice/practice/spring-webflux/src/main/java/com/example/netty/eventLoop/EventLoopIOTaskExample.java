package com.example.netty.eventLoop;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventLoopIOTaskExample {

    public static void main(String[] args) {
        var channel = new NioServerSocketChannel();
        var eventLoopGroup = new NioEventLoopGroup(1);
        eventLoopGroup.register(channel);

        channel.bind(new InetSocketAddress(8080))
            .addListener(future -> {
                if (future.isSuccess()) {
                    log.info("bind success");
                } else {
                    log.info("bind fail");
                    eventLoopGroup.shutdownGracefully();
                }
            });
    }
}
