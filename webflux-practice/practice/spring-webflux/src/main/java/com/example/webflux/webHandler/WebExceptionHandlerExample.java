package com.example.webflux.webHandler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

@Slf4j
public class WebExceptionHandlerExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        var webHandler = new WebHandler() {
            @Override
            public Mono<Void> handle(ServerWebExchange exchange) {
                final ServerHttpResponse response = exchange.getResponse();

                return response.writeWith(
                    Mono.create(sink -> {
                            sink.error(new CustomException("custom exception"));
                        }
                    ));
            }
        };

        // WebHandler 에서 에러가 발생한 경우, WebExceptionHandler 에 exchange 와 throwable 을 전달
        var exceptionHandler = new WebExceptionHandler() {
            @Override
            public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
                final ServerHttpResponse response = exchange.getResponse();

                if (ex instanceof CustomException) {
                    response.setStatusCode(HttpStatus.BAD_REQUEST);
                    var respBody = response.bufferFactory().wrap(ex.getMessage().getBytes());
                    // 상태를 변경하고 body 를 write 하거나
                    return response.writeWith(Mono.just(respBody));
                } else {
                    // 다음 exceptionHandler 에게 pass
                    return Mono.error(ex);
                }
            }
        };

        final HttpHandler webHttpHandler = WebHttpHandlerBuilder
            .webHandler(webHandler)
            .exceptionHandler(exceptionHandler)
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
