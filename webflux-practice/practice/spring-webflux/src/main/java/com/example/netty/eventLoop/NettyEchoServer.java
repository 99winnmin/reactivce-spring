package com.example.netty.eventLoop;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyEchoServer {
    // echoHandler 에서 ctx.fireChannelRead 를 호출하지 않았기 때문에 Tail context 에게 read event 전달 중단
    // Head 는 ChannelOutboundHandler 를 구현했기 때문에 최종적으로 channel 에 write 하는 역할을 담당
    private static ChannelInboundHandler echoHandler() {
        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                if (msg instanceof ByteBuf) {
                    try {
                        // inboud 된 message 를 읽어서 출력하고
                        var buf  = (ByteBuf) msg;
                        var len = buf.readableBytes();
                        var charset = StandardCharsets.UTF_8;
                        var body = buf.readCharSequence(len, charset);
                        log.info("EchoHandler.channelRead: {}", body);

                        // copy 를 통해서 새로운 ByteBuf 를 생성
                        buf.readerIndex(); // rewind
                        var result = buf.copy();
                        // 다음 handler 에게 메시지 전달
                        ctx.writeAndFlush(result) // channelFuture 를 반환하고 완료되면 리스너를 통해 channel close 수행
                            .addListener(ChannelFutureListener.CLOSE);
                    } finally {
                        // 메시지 전달 이후 리소스 해제
                        ReferenceCountUtil.release(msg);
                    }
                }
            }
        };
    }

    private static ChannelInboundHandler acceptor(EventLoopGroup childGroup) {
        var executorGroup = new DefaultEventExecutorGroup(4);
        return new ChannelInboundHandlerAdapter() {
            // serverSocketChannel 에 등록된 channelRead 는 java nio 에서 accept 를 통해
            // socketChannel 을 얻는 것과 동일
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                log.info("Acceptor.channelRead");

                if (msg instanceof SocketChannel) {
                    SocketChannel socketChannel = (SocketChannel) msg;
                    // socketChannel 에 LoggingHandler 를 등록
                    // 별도의 스레드에서 로깅 핸들러를 돌리겠따
                    socketChannel.pipeline().addLast(
                        executorGroup, new LoggingHandler(LogLevel.INFO));
                    // socketChannel 에 echoHandler 를 등록
                    socketChannel.pipeline().addLast(echoHandler());
                    // childGroup(read 이벤트 담당) 에 socketChannel 을 등록
                    childGroup.register(socketChannel);
                }
            }
        };
    }

    public static void main(String[] args) {
        // accept 이벤트를 처리하는 EventLoopGroup
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // read 이벤트를 처리하는 EventLoopGroup
        EventLoopGroup childGroup = new NioEventLoopGroup(4);

        NioServerSocketChannel serverSocketChannel = new NioServerSocketChannel();
        // parentGroup 에 serverSocketChannel 을 등록
        parentGroup.register(serverSocketChannel);
        // channel 의 pipeline 에 acceptor 를 등록
        serverSocketChannel.pipeline().addLast(acceptor(childGroup));

        // I/O 이벤트는 Head -> loggingHandler -> echoHandler 순으로 전달
        // I/O 작업은 echoHandler -> loggingHandler -> Head 순으로 전달
        serverSocketChannel.bind(new InetSocketAddress(8080))
            .addListener(future -> {
                if (future.isSuccess()) {
                    log.info("bind success");
                }
            });
    }
}
