package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class SingleSchedulerExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        // 아무런 설정을 하지 않았다면 publisher 는 subscribe 를 호출할 caller 의 쓰레드에서 실행
        for (int i = 0; i < 100; i++) {
            final int idx = i;
            Flux.create(sink -> {
                log.info("next: {}", idx);
                sink.next(idx);
            }).subscribeOn(
                // 여러곳에서 호출이 되더라도 캐싱된 1개 크기의 스레드풀을 제공
                // 1만개의 Flux 를 실행하더라도!
                // 모든 publish, subscribe 는 동일한 스레드에서 실행
                Schedulers.single()
            ).subscribe(value -> {
                log.info("value: " + value);
            });
        }
        //11:10 [single-1] - next: 0
        //11:10 [single-1] - value: 0
        //11:10 [single-1] - next: 1
        //11:10 [single-1] - value: 1
        //11:10 [single-1] - next: 2
        //11:10 [single-1] - value: 2
        //11:10 [single-1] - next: 3
        //11:10 [single-1] - value: 3
        Thread.sleep(1000);
        log.info("end main");
    }
}
