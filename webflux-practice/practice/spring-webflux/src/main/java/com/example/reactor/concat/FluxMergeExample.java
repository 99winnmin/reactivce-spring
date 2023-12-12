package com.example.reactor.concat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class FluxMergeExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        var flux1 = Flux.range(1, 3)
            .doOnSubscribe(value -> {
                log.info("doOnSubscribe1");
            })
            .delayElements(Duration.ofMillis(100));
        var flux2 = Flux.range(10, 3)
            .doOnSubscribe(value -> {
                log.info("doOnSubscribe2");
            })
            .delayElements(Duration.ofMillis(100));
        // Publisher 를 다른 Publisher 와 합치는 연산자
        // 모든 Publisher 를 바로 subscribe 하고 onNext 이벤트를 전파
        // 각각의 Publisher 의 순서는 보장되지 않음
        Flux.merge(flux1, flux2)
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribe();
        Thread.sleep(1000);
        log.info("end main");
        //13:57.114 [main] - doOnSubscribe1
        //13:57.116 [main] - doOnSubscribe2
        //13:57.218 [parallel-2] - doOnNext: 10
        //13:57.220 [parallel-1] - doOnNext: 1
        //13:57.321 [parallel-3] - doOnNext: 11
        //13:57.321 [parallel-3] - doOnNext: 2
        //13:57.423 [parallel-5] - doOnNext: 3
        //13:57.424 [parallel-5] - doOnNext: 12
        //13:58.119 [main] - end main
    }
}
