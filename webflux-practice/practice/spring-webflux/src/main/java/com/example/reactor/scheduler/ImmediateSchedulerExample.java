package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ImmediateSchedulerExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        Flux.create(sink -> {
            for (int j = 1; j <= 5; j++) {
                log.info("next: {}", j);
                sink.next(j);
            }
        }).subscribeOn(
            // subscribe 를 호출한 caller 스레드에서 즉시 실행
            // 별로도 스케쥴러를 넘기지 않았을 때 Scheduler.immediate() 사용
            Schedulers.immediate()
        ).subscribe(value -> {
            log.info("value: " + value);
        });
        log.info("end main");
        Thread.sleep(1000);
    }
}
