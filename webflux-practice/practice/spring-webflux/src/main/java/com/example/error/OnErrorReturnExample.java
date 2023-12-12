package com.example.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class OnErrorReturnExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.error(new RuntimeException("error"))
            // onError 이벤트를 처리하기 위해 고정된 값을 반환
            .onErrorReturn(0)
            .subscribe(value -> {
                log.info("value: " + value);
            });
        log.info("end main");
    }
}
