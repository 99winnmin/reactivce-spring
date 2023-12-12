package com.example.netty.eventLoop;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventLoopNIOTaskExample {

    public static void main(String[] args) {
        var eventLoopGroup = new NioEventLoopGroup(5);

        for(int i=0 ; i< 12; i++) {
            final int idx = i;
            eventLoopGroup.execute(() -> {
                log.info("task {}", idx);
            });
        }
        eventLoopGroup.shutdownGracefully();
    }
}
