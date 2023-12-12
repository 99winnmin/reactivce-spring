package com.example.reactor.concat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
public class DelayedElementsLateNextExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        Flux.create(
                sink -> {
                    for (int i = 1; i <= 5; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        sink.next(i);
                    }
                    sink.complete();
                })
            // 세팅된 시간보다 늦는다면 도착하자마자 onNext 이벤트를 전달
            .delayElements(Duration.ofMillis(500))
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribeOn(Schedulers.single())
            .subscribe();
        Thread.sleep(6000);
        log.info("end main");
        //09:05.367 [parallel-1] - doOnNext: 1
        //09:06.375 [parallel-2] - doOnNext: 2
        //09:07.372 [parallel-3] - doOnNext: 3
        //09:08.376 [parallel-4] - doOnNext: 4
        //09:09.381 [parallel-5] - doOnNext: 5
        //09:09.846 [main] - end main
    }
}
