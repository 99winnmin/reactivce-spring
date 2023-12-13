package com.example.webflux.httpHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

@Slf4j
public class HttpHandlerExample {
    public static void main(String[] args) throws InterruptedException {
        log.info("start main");
        var httpHandler = new HttpHandler() {
            @Override
            public Mono<Void> handle(
                ServerHttpRequest request,
                ServerHttpResponse response) {
                String nameQuery = request.getQueryParams().getFirst("name");
                String name = nameQuery == null ? "world" : nameQuery;

                String content = "Hello " + name;
                log.info("responseBody: {}", content);
                Mono<DataBuffer> responseBody = Mono.just(
                    response.bufferFactory()
                        .wrap(content.getBytes())
                );

                response.addCookie(ResponseCookie.from("name", name).build());
                response.getHeaders().add("Content-Type", "text/plain");
                return response.writeWith(responseBody);
            }
        };

        var adapter = new ReactorHttpHandlerAdapter(httpHandler);
        // 아래 방식은 reactor netty 임
            // 별도의 channel 과 eventLoopGroup 을 명시하지 않음
            // read I/O event 가 완료되었을 때
                // netty 에서는 pipeline 에 ChannelHandler 를 추가
                // reactor netty 에서는 httpHandler 를 구현하여 Reactor~Adapter 로 감싸서 전달
        HttpServer.create()
            .host("localhost")
            .port(8080)
            .handle(adapter)
            .bindNow()
            .channel().closeFuture().sync();

        // But 한계점이 명확함
            // request header 나 body 를 변경하는 작업을 HttpHandler 에 직접 구현해야함
            // 모든 error 를 직접 catch 해서 관리해야함
            // request 의 read, response 의 write 모두 구현해야함
    }
}
