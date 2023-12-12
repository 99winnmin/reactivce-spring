package com.example.reactor.sequence;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class SequenceGenerateTwiceExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.generate(
            () -> 0,
            (state, sink) -> {
                sink.next(state);
                sink.next(state); // 한번의 generate 에서 next 를 두번 호출하면 에러 발생
                if (state == 9) {
                    sink.complete();
                }
                return state + 1;
            }
        ).subscribe(value -> {
            log.info("value: " + value);
        }, error -> {
            log.error("error: " + error);
        }, () -> {
            log.info("complete");
        });
        //23:20 [main] - value: 0
        //23:20 [main] - error: java.lang.IllegalStateException: More than one call to onNext
    }
}
