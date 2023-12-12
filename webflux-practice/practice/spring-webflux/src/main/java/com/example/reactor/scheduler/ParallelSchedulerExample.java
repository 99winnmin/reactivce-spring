package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ParallelSchedulerExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        for (int i = 0; i < 100; i++) {
            final int idx = i;
            Flux.create(sink -> {
                log.info("next: {}", idx);
                sink.next(idx);
            }).subscribeOn(
                // 캐싱된 n개 크기의 스레드풀을 제공
                // 기본적으로 CPU 코어 수 만큼의 크기를 갖음
                Schedulers.parallel()
            ).subscribe(value -> {
                log.info("value: " + value);
            });
        }
        //11:49 [parallel-4] - value: 3
        //11:49 [parallel-3] - next: 10
        //11:49 [parallel-8] - next: 15
        //11:49 [parallel-4] - next: 11
        //11:49 [parallel-8] - value: 15
        //11:49 [parallel-3] - value: 10
        //11:49 [parallel-8] - next: 23
        //11:49 [parallel-7] - value: 6
        //11:49 [parallel-8] - value: 23
        Thread.sleep(2000);
        log.info("end main");
    }
}
