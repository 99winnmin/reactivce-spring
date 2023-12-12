package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class PublishOnSubscribeOnExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        Flux.create(sink -> {
            for (var i = 0; i < 5; i++) {
                log.info("next: {}", i);
                sink.next(i); // parallel 에서 실행
            }
        }).publishOn(
            Schedulers.single()
        ).doOnNext(item -> {
            log.info("doOnNext: {}", item);
        }).publishOn(
            Schedulers.boundedElastic()
        ).doOnNext(item -> {
            log.info("doOnNext2: {}", item);
        })
            // subscribeOn 을 인자로 받음
            // source 의 실행 스레드에 영향을 줌
            .subscribeOn(Schedulers.parallel()
        ).subscribe(value -> {
            log.info("value: " + value);
        });
        Thread.sleep(1000);
        log.info("end main");
    }
}
