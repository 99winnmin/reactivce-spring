package com.example.webflux.httpHandler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.RequestPath;

import java.net.URI;

@Slf4j
public class RequestPathExample {
    @SneakyThrows
    public static void main(String[] args) {
        URI uri = new URI("http://localhost:8080/app/api/hello?name=taewoo#home");
        RequestPath path = RequestPath.parse(uri, "/app");
        // ContextPath 와 application 내부의 path 를 분리해서 제공
        // spring webflux 에선 기본적으로 root context path("/") 를 갖음
            // spring.webflux.base-path 프로퍼티에서 변경 가능
        log.info("path.pathWithinApplication(): {}", path.pathWithinApplication());
        log.info("path.contextPath(): {}", path.contextPath());
        //51:42.136 [main] - path.pathWithinApplication(): /api/hello
        //51:42.139 [main] - path.contextPath(): /app
    }
}
