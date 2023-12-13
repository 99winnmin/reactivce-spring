package com.example.webflux.webfilter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

@Slf4j
public class WebFilterExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        var webHandler = new WebHandler() {
            @Override
            public Mono<Void> handle(ServerWebExchange exchange) {
                log.info("web handler");
                final ServerHttpRequest request = exchange.getRequest();
                final ServerHttpResponse response = exchange.getResponse();

                final String nameHeader = request.getHeaders()
                    .getFirst("X-Custom-Name");
                log.info("X-Custom-Name: {}", nameHeader);
                String name = exchange.getAttribute("name");
                String content = "Hello " + name;
                Mono<DataBuffer> responseBody = Mono.just(
                    response.bufferFactory().wrap(content.getBytes())
                );

                response.getHeaders()
                    .add("Content-Type", "text/plain");
                return response.writeWith(responseBody);
            }
        };

        // WebHandler 를 실행하기 앞서서 실행
        // webHandler 에게 전달될 ServerWebExchange 와 다음 filter 혹은 handler 를 실행하기 위한 WebFilterChain
        // chain, filter 를 호출하여, 요청을 다음 filter 에게 넘기거나 handler 를 실행 가능
        var extractNameFromHeaderFilter = new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                log.info("extractNameFromHeaderFilter");
                final ServerHttpRequest request = exchange.getRequest();
                final ServerHttpResponse response = exchange.getResponse();

                String name = request.getHeaders()
                    .getFirst("X-Custom-Name");

                if (name == null) {
                    response.setStatusCode(HttpStatus.BAD_REQUEST);
                    return response.setComplete();
                } else {
                    exchange.getAttributes().put("name", name);
                    // mutate 를 통해 ServerHttpRequest 는 각각을 Builder 로 변경할 수 있음
                    var newReq = request.mutate()
                        .headers(h -> h.remove("X-Custom-Name"))
                        .build();
                    return chain.filter(
                        exchange.mutate().request(newReq).build()
                    );
                }
            }
        };

        var timeLoggingFilter = new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                log.info("timeLoggingFilter");
                long startTime = System.nanoTime();
                return chain.filter(exchange)
                    .doOnSuccess(v -> {
                        long endTime = System.nanoTime();
                        log.info("time: {} ms", (endTime - startTime)/1000000.0);
                    });
            }
        };

        final HttpHandler webHttpHandler = WebHttpHandlerBuilder
            .webHandler(webHandler)
            .filter(
                extractNameFromHeaderFilter,
                timeLoggingFilter
            )
            .build();

        final var adapter = new ReactorHttpHandlerAdapter(webHttpHandler);
        HttpServer.create()
            .host("localhost")
            .port(8080)
            .handle(adapter)
            .bindNow()
            .channel().closeFuture().sync();
    }
}
