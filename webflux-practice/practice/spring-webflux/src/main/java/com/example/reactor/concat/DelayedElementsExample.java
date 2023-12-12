package com.example.reactor.concat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
public class DelayedElementsExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        Flux.create(
                sink -> {
                    for (int i = 1; i <= 5; i++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        sink.next(i);
                    }
                    sink.complete();
                })
            // 최소 delay 만큼의 간격을 두고 onNext 이벤트를 발행
            // onNext 이벤트가 발생한 후 delay 보다 더 늦게 다음 onNext 이벤트가 전달되면 바로 전파
            // 100ms 간격으로 발행되지만 500ms 간격으로 전달되도록 변경
            .delayElements(Duration.ofMillis(500))
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribeOn(Schedulers.single())
            .subscribe();
        Thread.sleep(6000);
        log.info("end main");
        //54:07.538 [parallel-1] - doOnNext: 1
        //54:08.050 [parallel-2] - doOnNext: 2
        //54:08.553 [parallel-3] - doOnNext: 3
        //54:09.054 [parallel-4] - doOnNext: 4
        //54:09.557 [parallel-5] - doOnNext: 5
        //54:12.927 [main] - end main
    }
}
