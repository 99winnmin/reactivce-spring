package com.example.reactor.concat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class FluxConcatExample {
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
        // 앞의 Publisher 가 onComplete 이벤트를 전달하면 다음 Publisher 를 subscribe
        // 각각 Publisher 의 onNext 이벤트가 전파
        // 따라서 Publisher 의 순서가 보장
        Flux.concat(flux1, flux2)
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribe();
        Thread.sleep(1000);
        log.info("end main");
        //12:03.199 [main] - doOnSubscribe1
        //12:03.302 [parallel-1] - doOnNext: 1
        //12:03.407 [parallel-2] - doOnNext: 2
        //12:03.512 [parallel-3] - doOnNext: 3
        //12:03.513 [parallel-3] - doOnSubscribe2
        //12:03.622 [parallel-4] - doOnNext: 10
        //12:03.723 [parallel-5] - doOnNext: 11
        //12:03.823 [parallel-6] - doOnNext: 12
        //12:04.206 [main] - end main
    }
}