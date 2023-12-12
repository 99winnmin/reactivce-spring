package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Slf4j
public class ExecutorServiceSchedulerExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        var executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100; i++) {
            final int idx = i;
            Flux.create(sink -> {
                log.info("next: {}", idx);
                sink.next(idx);
            }).subscribeOn(
                // 이미 존재하는 ExecutorService 를 Scheduler 생성
                Schedulers.fromExecutorService(executorService)
            ).subscribe(value -> {
                log.info("value: " + value);
            });
        }
        Thread.sleep(1000);
        executorService.shutdown();
        log.info("end main");
        //21:10 [pool-2-thread-1] - value: 51
        //21:10 [pool-2-thread-1] - next: 52
        //21:10 [pool-2-thread-1] - value: 52
        //21:10 [pool-2-thread-1] - next: 53
        //21:10 [pool-2-thread-1] - value: 53
        //21:10 [pool-2-thread-1] - next: 54
        //21:10 [pool-2-thread-1] - value: 54
    }
}
