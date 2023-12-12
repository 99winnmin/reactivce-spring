package com.example.reactor.util;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class CacheExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux<Object> flux = Flux.create(sink -> {
            for (int i = 0; i < 3; i++) {
                log.info("next: " + i);
                sink.next(i);
            }
            log.info("complete in publisher");
            sink.complete();
        }).cache(); // publisher 를 좀 다르게 행동하게 함
        // 처음 subscribe 에만 publisher 를 실행
        // 그 이후 subscribe 부터는 저장한 event 를 순서대로 전달

        flux.subscribe(value -> {
            log.info("value: " + value);
        }, null, () -> {
            log.info("complete");
        });
        //32:59.849 [main] - next: 0
        //32:59.849 [main] - value: 0
        //32:59.849 [main] - next: 1
        //32:59.849 [main] - value: 1
        //32:59.849 [main] - next: 2
        //32:59.849 [main] - value: 2
        //32:59.849 [main] - complete in publisher
        //32:59.849 [main] - complete

        flux.subscribe(value -> {
            log.info("value: " + value);
        }, null, () -> {
            log.info("complete");
        });
        // 두번째부터는 publisher 를 실행하지 않고 저장한 event 를 전달
        //32:59.849 [main] - value: 0
        //32:59.849 [main] - value: 1
        //32:59.849 [main] - value: 2
        //32:59.849 [main] - complete

        log.info("end main");
    }
}
