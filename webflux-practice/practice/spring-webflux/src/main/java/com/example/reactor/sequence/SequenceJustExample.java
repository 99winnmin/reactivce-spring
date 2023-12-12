package com.example.reactor.sequence;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class SequenceJustExample {
    public static void main(String[] args) {
        log.info("start main");
        // just 를 통해서 주어진 객체를 subscribe 시점에 전달
        Mono.just(1)
            .subscribe(value -> {
                log.info("value: " + value);
            });

        Flux.just(1, 2, 3, 4, 5)
            .subscribe(value -> {
                log.info("value: " + value);
            });
        log.info("end main");
        //08:26 [main] - value: 1
        //08:26 [main] - value: 1
        //08:26 [main] - value: 2
        //08:26 [main] - value: 3
        //08:26 [main] - value: 4
        //08:26 [main] - value: 5
        //08:26 [main] - end main
    }
}
