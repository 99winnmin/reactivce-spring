package com.example.reactor.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class SkipExample {
    @SneakyThrows
    public static void main(String[] args) {
        // take 함수의 반대 느낌
        log.info("start main");
        Flux.range(1, 10)
            // 처음 n 개의 onNext 이벤트를 무시하고 그 다음부터 전달
            .skip(5)
            .doOnNext(value -> {
                log.info("not skipped: " + value);
            })
            .subscribe();

        Flux.range(1, 10)
            // onComplete 이벤트가 발생하기 직전 n개의 아이템만 전파
            // 마지막 n 개의 onNext 이벤트만 전달
            .skipLast(5)
            .doOnNext(value -> {
                log.info("not skipped: " + value);
            })
            .subscribe();

        Thread.sleep(1000);
        log.info("end main");
        //27:46.475 [main] - not skipped: 6
        //27:46.475 [main] - not skipped: 7
        //27:46.475 [main] - not skipped: 8
        //27:46.475 [main] - not skipped: 9
        //27:46.475 [main] - not skipped: 10
        //27:46.476 [main] - not skipped: 1
        //27:46.476 [main] - not skipped: 2
        //27:46.476 [main] - not skipped: 3
        //27:46.476 [main] - not skipped: 4
        //27:46.476 [main] - not skipped: 5
        //27:47.477 [main] - end main
    }
}
