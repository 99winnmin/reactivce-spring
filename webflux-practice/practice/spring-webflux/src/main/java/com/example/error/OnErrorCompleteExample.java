package com.example.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class OnErrorCompleteExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.create(sink -> {
                sink.next(1);
                sink.next(2);
                sink.error(new RuntimeException("error"));
            })
            // onError 이벤트를 처리하기 위해 onComplete 이벤트를 발생시킴
            // error 이벤트가 complete 이벤트로 변경되었기 때문에 errorConsumer 가 동작하지 않음
            .onErrorComplete()
            .subscribe(value -> {
                    log.info("value: " + value);
                }, e -> {
                    log.info("error: " + e);
                }, () -> {
                    log.info("complete");
                }
            );
        log.info("end main");
        //40:23 [main] - value: 1
        //40:23 [main] - value: 2
        //40:23 [main] - complete
        //40:23 [main] - end main
    }
}
