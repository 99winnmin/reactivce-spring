package com.example.reactor.sequence;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class SequenceMonoFromExample {
    public static void main(String[] args) {
        log.info("start main");
        // Callable 함수형 인터페이스를 실행하고 반환 값을 onNext 로 전달
        Mono.fromCallable(() -> {
            return 1;
        }).subscribe(value -> {
            log.info("value fromCallable: " + value);
        });

        // Future 를 받아서 done 상태가 되면 반환값을 onNext 로 전달
        Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
            return 1;
        })).subscribe(value -> {
            log.info("value fromFuture: " + value);
        });

        // Supplier 함수형 인터페이스를 실행하고 반환 값을 onNext 로 전달
        Mono.fromSupplier(() -> {
            return 1;
        }).subscribe(value -> {
            log.info("value fromSupplier: " + value);
        });

        // Runnable 함수형 인터페이스를 실행하고(반환 값 없음) 완료 시 onComplete 이벤트를 전달
        // 특정 사건을 전달하는 경우
        Mono.fromRunnable(() -> {
            /* do nothing */
        }).subscribe(null, null, () -> {
            log.info("complete fromRunnable");
        });

        //12:56 [main] - value fromCallable: 1
        //12:56 [main] - value fromFuture: 1
        //12:56 [main] - value fromSupplier: 1
        //12:56 [main] - complete fromRunnable
        //12:56 [main] - end main

        log.info("end main");
    }
}
