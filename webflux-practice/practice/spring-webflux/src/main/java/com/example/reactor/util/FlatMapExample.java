package com.example.reactor.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class FlatMapExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        Flux.range(1, 5)
            // onNext 이벤트를 받아서 publisher 를 반환
            // 반환된 publisher 의 이벤트를 아래로 전달
                // 여러 publisher 를 조합해야하는 경우 유용
            // concat 과 유사
            .flatMap(value -> {
                return Flux.range(1, 2)
                    .map(value2 -> value + ", " + value2)
                    .publishOn(Schedulers.parallel());
            })
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribe();
        Thread.sleep(1500);
        log.info("end main");
        //22:23.779 [parallel-1] - doOnNext: 1, 1
        //22:23.779 [parallel-1] - doOnNext: 1, 2
        //22:23.780 [parallel-2] - doOnNext: 2, 1
        //22:23.780 [parallel-2] - doOnNext: 2, 2
        //22:23.780 [parallel-2] - doOnNext: 3, 1
        //22:23.780 [parallel-2] - doOnNext: 3, 2
        //22:23.784 [parallel-4] - doOnNext: 4, 1
        //22:23.784 [parallel-4] - doOnNext: 4, 2
        //22:23.784 [parallel-4] - doOnNext: 5, 1
        //22:23.784 [parallel-4] - doOnNext: 5, 2
        //22:25.289 [main] - end main

    }
}
