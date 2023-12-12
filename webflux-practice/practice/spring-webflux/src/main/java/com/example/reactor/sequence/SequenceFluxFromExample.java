package com.example.reactor.sequence;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class SequenceFluxFromExample {
    public static void main(String[] args) {
        log.info("start main");
        // Iterable 를 받아서 각각의 item 을 onNext 로 전달
        Flux.fromIterable(
            List.of(1, 2, 3, 4, 5)
        ).subscribe(value -> {
            log.info("value: " + value);
        });

        // Stream 을 받아서 각각의 item 을 onNext 로 전달
        Flux.fromStream(
            IntStream.range(1, 6).boxed()
        ).subscribe(value -> {
            log.info("value: " + value);
        });

        // Array 를 받아서 각각의 item 을 onNext 로 전달
        Flux.fromArray(
            new Integer[]{1, 2, 3, 4, 5}
        ).subscribe(value -> {
            log.info("value: " + value);
        });

        // start 부터 시작해서 한개씩 커진 값을 n개 만큼 onNext 로 전달
        Flux.range(
            1, 5
        ).subscribe(value -> {
            log.info("value: " + value);
        });

        // 각각 모두 다
        //14:15 [main] - value: 1
        //14:15 [main] - value: 2
        //14:15 [main] - value: 3
        //14:15 [main] - value: 4
        //14:15 [main] - value: 5
        log.info("end main");
    }
}
