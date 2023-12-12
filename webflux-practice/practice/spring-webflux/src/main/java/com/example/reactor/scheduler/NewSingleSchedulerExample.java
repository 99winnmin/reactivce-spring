package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class NewSingleSchedulerExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        for (int i = 0; i < 100; i++) {
            var newSingle = Schedulers.newSingle("single");
            final int idx = i;
            Flux.create(sink -> {
                log.info("next: {}", idx);
                sink.next(idx);
            }).subscribeOn(
                // single, parallel, boundedElastic 모두 캐싱된 스레드풀을 제공
                // 모두 새로운 스레드풀을 만들어서 제공
                newSingle
            ).subscribe(value -> {
                log.info("value: " + value);
                // dispose 로 스레드풀을 종료해야함
                newSingle.dispose();
            });
        }
        Thread.sleep(1000);
        log.info("end main");
    }
}
