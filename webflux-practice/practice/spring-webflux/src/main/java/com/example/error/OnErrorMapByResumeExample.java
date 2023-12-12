package com.example.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Slf4j
public class OnErrorMapByResumeExample {
    private static class CustomBusinessException
        extends RuntimeException {
        public CustomBusinessException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        log.info("start main");
        Flux.error(new IOException("fail to read file"))
            // onErrorResume 과 Flux.error(혹은 Mono.error)를 사용하면 에러를 다른 에러로 변환하여 전달 가능
            .onErrorResume(e ->
                Flux.error(new CustomBusinessException("custom")))
            .subscribe(value -> {
                    log.info("value: " + value);
                }, e -> {
                    log.info("error: " + e);
                }
            );
        log.info("end main");
    }
}
