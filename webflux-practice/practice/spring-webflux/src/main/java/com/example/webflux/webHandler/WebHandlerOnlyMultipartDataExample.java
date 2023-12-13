package com.example.webflux.webHandler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseCookie;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

@Slf4j
public class WebHandlerOnlyMultipartDataExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        var webHandler = new WebHandler() {
            @Override
            public Mono<Void> handle(ServerWebExchange exchange) {
                final ServerHttpResponse response = exchange.getResponse();

                // MultipartData 는 여러 part 로 구성, plain text 혹은 파일등을 보낼 수 있고 boundary 로 구분
                    // Part 인터페이스를 구현한 FormFieldPart 와 FilePart 로 구분
                    // FormField 는 plain text 필드에 해당. value()를 통해서 값에 접근 가능
                    // FilePart 는 file 필에 해당. filename() 으로 파일명에 접근하고, transferTo 로 로컬에 file 필드의 내용 전달
                return exchange.getMultipartData().map(multipartData -> {
                    return ((FormFieldPart)multipartData.getFirst("name")).value();
                }).flatMap(nameQuery -> {
                    String name = nameQuery == null ? "world" : nameQuery;

                    String content = "Hello " + name;
                    log.info("responseBody: {}", content);
                    Mono<DataBuffer> responseBody = Mono.just(
                        response.bufferFactory()
                            .wrap(content.getBytes())
                    );

                    response.addCookie(
                        ResponseCookie.from("name", name).build());
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
