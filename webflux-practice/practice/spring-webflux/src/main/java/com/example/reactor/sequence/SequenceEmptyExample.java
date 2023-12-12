package com.example.reactor.sequence;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class SequenceEmptyExample {
    public static void main(String[] args) {
        log.info("start main");
        // 빈값 혹은 사건 완료만 전달
        // empty 를 통해서 subscriber 에게 onComplete 이벤트만 전달
        Mono.empty()
            .subscribe(value -> {
                log.info("value: " + value);
            }, null, () -> {
                log.info("complete");
            });
        Flux.empty()
            .subscribe(value -> {
                log.info("value: " + value);
            }, null, () -> {
                log.info("complete");
            });
        //10:16 [main] - complete
        //10:16 [main] - complete
        //10:16 [main] - end main
        log.info("end main");
    }
}
