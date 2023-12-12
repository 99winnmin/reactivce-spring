package com.example.reactor.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class TakeExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        Flux.range(1, 10)
            // 대략 n개까지 onNext 이벤트 전파하고 n개에 도달하면 onComplete 이벤트 전파
            .take(5)
            .doOnNext(value -> {
                log.info("take: " + value);
            })
            .subscribe();

        Flux.range(1, 10)
            // onComplete 이벤트가 발생하기 직전 n개의 아이템만 전파
            // 맨마지막 5개만 전파
            .takeLast(5)
            .doOnNext(value -> {
                log.info("takeLast: " + value);
            })
            .subscribe();

        Thread.sleep(1000);
        log.info("end main");
        //25:25.251 [main] - take: 1
        //25:25.252 [main] - take: 2
        //25:25.252 [main] - take: 3
        //25:25.252 [main] - take: 4
        //25:25.252 [main] - take: 5
        //25:25.252 [main] - takeLast: 6
        //25:25.252 [main] - takeLast: 7
        //25:25.252 [main] - takeLast: 8
        //25:25.252 [main] - takeLast: 9
        //25:25.252 [main] - takeLast: 10
        //25:26.257 [main] - end main

    }
}
