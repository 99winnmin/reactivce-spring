package com.example.reactor.util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FilterExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.range(1, 5)
            // onNext 이벤트를 받아서 조건에 맞는 값만 아래로 전달
            // true 라면 onNext, false 라면 무시
            // handle 과 유사
            .filter(value -> value % 2 == 0)
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribe();
        log.info("end main");
        //23:44.685 [main] - doOnNext: 2
        //23:44.685 [main] - doOnNext: 4
        //23:44.685 [main] - end main
    }
}
