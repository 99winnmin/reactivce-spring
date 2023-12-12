package com.example.reactor.concat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class FluxMergeSequentialExample {
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
        // merge 와 다르게 내부에서 재정렬하여 순서를 보장
            // 내부에 저장해놓음 -> 앞에꺼가 complete 되는 시점에 뒤에꺼를 subscribe
        Flux.mergeSequential(flux1, flux2)
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribe();

        Thread.sleep(1000);
        log.info("end main");
        //15:56.464 [main] - doOnSubscribe1
        //15:56.465 [main] - doOnSubscribe2
        //15:56.566 [parallel-1] - doOnNext: 1
        //15:56.670 [parallel-4] - doOnNext: 2
        //15:56.774 [parallel-5] - doOnNext: 3
        //15:56.775 [parallel-5] - doOnNext: 10
        //15:56.775 [parallel-5] - doOnNext: 11
        //15:56.775 [parallel-5] - doOnNext: 12
        //15:57.470 [main] - end main
    }
}
