package com.example.reactor.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class DeferExample {
    public static void main(String[] args) {
        log.info("start main");
        // publisher 를 생성하는 Consumer 를 인자로 받아서 publisher 를 생성하고
        // 생성된 publisher 의 이벤트를 아래로 전달
        Mono.defer(() -> {
            return Mono.just(1);
        }).subscribe(n -> {
            log.info("next: {}", n);
        });
        log.info("end main");
        //57:08.456 [main] - start main
        //57:08.477 [main] - next: 1
        //57:08.477 [main] - end main
    }
}
