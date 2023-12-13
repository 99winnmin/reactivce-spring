package com.example.webflux.webfilter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

@Slf4j
public class WebFilterPrePostExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        var webHandler = new WebHandler() {
            @Override
            public Mono<Void> handle(ServerWebExchange exchange) {
                log.info("web handler");
                final ServerHttpResponse response = exchange.getResponse();

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

        // chain.filter 를 호출하기 전에 로직을 수행하면
        // servlet 스택 HandlerInterceptor 의 preHandle 과 동일
        var preFilter = new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                log.info("pre filter");
                return chain.filter(exchange);
            }
        };

        // chain.filter 를 호출한 후 chaining 을 하여 로직을 수행하면
        // HandlerInterceptor 의 postHandle 과 동일
        var afterFilter = new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                return chain.filter(exchange)
                    .doOnSuccess(v -> {
                        log.info("after filter");
                    });
            }
        };
        //48:40.854 [reactor-http-nio-2] - pre filter
        //48:40.854 [reactor-http-nio-2] - web handler
        //48:40.855 [reactor-http-nio-2] - after filter

        final HttpHandler webHttpHandler = WebHttpHandlerBuilder
            .webHandler(webHandler)
            .filter(preFilter, afterFilter)
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
