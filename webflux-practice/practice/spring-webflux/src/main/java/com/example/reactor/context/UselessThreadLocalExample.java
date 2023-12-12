package com.example.reactor.context;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class UselessThreadLocalExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        // 하나의 스레드에 값을 저장하고 해당 스레드 내에서 어디서든지 접근가능
        // 만약 subscribeOn, publishOn 으로 실행 스레드가 달라진다면?
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("seungmin");

        Flux.create(sink -> {
            log.info("threadLocal: " + threadLocal.get());
            sink.next(1);
        }).publishOn(Schedulers.parallel()
        ).map(value -> {
            log.info("threadLocal: " + threadLocal.get());
            return value;
        }).publishOn(Schedulers.boundedElastic()
        ).map(value -> {
            log.info("threadLocal: " + threadLocal.get());
            return value;
        }).subscribeOn(Schedulers.single()
        ).subscribe();
        //45:16.930 [main] - start main
        //45:16.983 [single-1] - threadLocal: null
        //45:16.983 [parallel-1] - threadLocal: null
        //45:16.984 [boundedElastic-1] - threadLocal: null
        //45:17.983 [main] - end main

        Thread.sleep(1000);
        log.info("end main");
    }
}
