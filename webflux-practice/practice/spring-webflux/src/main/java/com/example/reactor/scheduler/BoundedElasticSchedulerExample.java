package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class BoundedElasticSchedulerExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        for(int i = 0; i < 200; i++) {
            final int idx = i;
            Flux.create(sink -> {
                log.info("next: {}", idx);
                sink.next(idx);
            }).subscribeOn(
                // 캐싱된 고정되지 않은 크기의 스레드풀을 제공 -> 동적으로 스레드풀 관리
                // 재사용할 수 있는 스레드가 있다면 사용하고, 없으면 새로 생성
                // 특정시간(60초) 사용하지 않으면 제거
                // 생성 가능한 스레드 수 제한(기본 cpu * 10 )
                // I/O blocking 작업에 적합
                Schedulers.boundedElastic()
            ).subscribe(value -> {
                log.info("value: " + value);
            });
        }
        Thread.sleep(4000);
        log.info("end main");
        //13:39 [boundedElastic-39] - value: 198
        //13:39 [boundedElastic-35] - value: 194
        //13:39 [boundedElastic-37] - value: 196
        //13:39 [boundedElastic-29] - value: 108
        //13:39 [boundedElastic-31] - next: 110
        //13:39 [boundedElastic-31] - value: 110
    }
}
