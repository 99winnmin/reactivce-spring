package com.example.reactor.util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class DoOnXXExample {
    public static void main(String[] args) {
        log.info("start main");
        // doOnXX 들은 각각의 이벤트마다 흐름에 영향을 주지 않고
        // 위에서 내려오는 이벤트에 대해서 로깅이나 추가 작업 가능
        Flux.range(1, 5)
            .map(value -> value * 2)
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .doOnComplete(() -> {
                log.info("doOnComplete");
            })
            .doOnSubscribe(subscription -> {
                log.info("doOnSubscribe");
            })
            .doOnRequest(value -> {
                // 별도의 Consumer 를 등록하지 않으면 Unbounded request 임
                log.info("doOnRequest: " + value);
            })
            .map(value -> value / 2)
            .subscribe();
        log.info("end main");
        //19:50.525 [main] - doOnSubscribe
        //19:50.526 [main] - doOnRequest: 9223372036854775807
        //19:50.526 [main] - doOnNext: 2
        //19:50.526 [main] - doOnNext: 4
        //19:50.526 [main] - doOnNext: 6
        //19:50.526 [main] - doOnNext: 8
        //19:50.526 [main] - doOnNext: 10
        //19:50.526 [main] - doOnComplete
        //19:50.526 [main] - end main
    }
}
