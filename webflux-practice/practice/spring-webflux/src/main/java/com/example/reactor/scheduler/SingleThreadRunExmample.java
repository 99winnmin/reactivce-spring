package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Slf4j
public class SingleThreadRunExmample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        // 아무런 설정을 하지 않았다면 publisher 는 subscribe 를 호출할 caller 의 쓰레드에서 실행
        var executor = Executors.newSingleThreadExecutor();
        try {
            executor.submit(() -> {
                Flux.create(sink -> {
                    for (int i = 1; i <= 5; i++) {
                        log.info("next: {}", i);
                        sink.next(i);
                    }
                }).subscribeOn(
                    Schedulers.single()
                ).subscribe(value -> { // subscribe 에 제공된 람다 혹은 스케쥴러 또한 caller 의 스레드에서 실행
                    log.info("value: " + value);
                });
            });
        } finally {
            executor.shutdown();
        }
        //07:02 [main] - start main
        //07:02 [pool-2-thread-1] - Using Slf4j logging framework
        //07:02 [single-1] - next: 1
        //07:02 [single-1] - value: 1
        //07:02 [single-1] - next: 2
        //07:02 [single-1] - value: 2
        //07:02 [single-1] - next: 3
        //07:02 [single-1] - value: 3
        //07:02 [single-1] - next: 4
        //07:02 [single-1] - value: 4
        //07:02 [single-1] - next: 5
        //07:02 [single-1] - value: 5
        Thread.sleep(2000);

        // Publish 혹은 Subscribe 에 어떤 스케줄러를 적용되었는가에 따라서 task 를 실행하는 스레드풀이 결정
        // 즉시 task 를 실행하거나 delay 를 두고 실행 가능
    }
}
