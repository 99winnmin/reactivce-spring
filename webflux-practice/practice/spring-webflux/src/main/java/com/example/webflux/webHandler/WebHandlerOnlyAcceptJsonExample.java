package com.example.webflux.webHandler;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

@Slf4j
public class WebHandlerOnlyAcceptJsonExample {
    @Data
    private static class NameHolder {
        private String name;
    }

    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        // codecConfigurer 를 생성하고 exchange 와 codecConfigurer 로 ServerRequest 를 만들어서
        var codecConfigurer = ServerCodecConfigurer.create();

        var webHandler = new WebHandler() {
            @Override
            public Mono<Void> handle(ServerWebExchange exchange) {
                final ServerRequest request = ServerRequest.create(
                    exchange,
                    codecConfigurer.getReaders()
                );
                final ServerHttpResponse response = exchange.getResponse();
                // serverRequest.bodyToMono 로 json 객체로 변경
                final var bodyMono = request.bodyToMono(NameHolder.class);
                // webHandler 에서는 json 을 다루기 쉽지 않다,,,
                return bodyMono.flatMap(nameHolder -> {
                    String nameQuery = nameHolder.name;
                    String name = nameQuery == null ? "world" : nameQuery;

                    String content = "Hello " + name;
                    log.info("responseBody: {}", content);
                    Mono<DataBuffer> responseBody = Mono.just(
                        response.bufferFactory()
                            .wrap(content.getBytes())
                    );

                    response.getHeaders()
                        .add("Content-Type", "text/plain");
                    return response.writeWith(responseBody);
                });
            }
        };

        final HttpHandler webHttpHandler = WebHttpHandlerBuilder
            .webHandler(webHandler)
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