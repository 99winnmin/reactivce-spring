package com.example.reactor.sequence;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class SequenceErrorExample {
    public static void main(String[] args) {
        log.info("start main");
        // error 를 통해서 subscriber 에게 onError 이벤트만 전달
        Mono.error(new RuntimeException("mono error"))
            .subscribe(value -> {
                log.info("value: " + value);
            }, error -> {
                log.error("error: " + error);
            });

        Flux.error(new RuntimeException("flux error"))
            .subscribe(value -> {
                log.info("value: " + value);
            }, error -> {
                log.error("error: " + error);
            });
        //08:47 [main] - error: java.lang.RuntimeException: mono error
        //08:47 [main] - error: java.lang.RuntimeException: flux error
        //08:47 [main] - end main
        log.info("end main");
    }
}
