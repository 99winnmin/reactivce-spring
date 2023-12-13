package com.example.webflux.httpHandler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
public class URIExample {
    @SneakyThrows
    public static void main(String[] args) {
        URI uri = new URI("http://abc:test@localhost:8080/api/hello?name=taewoo#home");

        log.info("uri.getScheme(): {}", uri.getScheme());
        log.info("uri.getSchemeSpecificPart(): {}", uri.getSchemeSpecificPart());
        log.info("uri.getAuthority(): {}", uri.getAuthority());
        log.info("uri.getUserInfo(): {}", uri.getUserInfo());
        log.info("uri.getHost(): {}", uri.getHost());
        log.info("uri.getPort(): {}", uri.getPort());
        log.info("uri.getPath(): {}", uri.getPath());
        log.info("uri.getQuery(): {}", uri.getQuery());
        log.info("uri.getFragment(): {}", uri.getFragment());

        //54:17.652 [main] - uri.getScheme(): http
        //54:17.654 [main] - uri.getSchemeSpecificPart(): //abc:test@localhost:8080/api/hello?name=taewoo
        //54:17.654 [main] - uri.getAuthority(): abc:test@localhost:8080
        //54:17.654 [main] - uri.getUserInfo(): abc:test
        //54:17.654 [main] - uri.getHost(): localhost
        //54:17.654 [main] - uri.getPort(): 8080
        //54:17.654 [main] - uri.getPath(): /api/hello
        //54:17.654 [main] - uri.getQuery(): name=taewoo
        //54:17.654 [main] - uri.getFragment(): home
    }
}
