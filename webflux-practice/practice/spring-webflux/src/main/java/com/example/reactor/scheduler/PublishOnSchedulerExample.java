package com.example.reactor.scheduler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class PublishOnSchedulerExample {
    @SneakyThrows
    public static void main(String[] args) {
        // publishOn 은 위치가 중요하고 subscribeOn 은 위치가 중요하지 않다.
        // A -> B -> C 순으로 연산자가 chaining 되어 있다면 별도의 publishOn 이 없다면,
        // A를 실행한 쓰레드가 B를 실행하고, B를 실행한 스레드가 C를 실행하는 등 스레드도 chaining 됨
        // source 가 실행되는 스레드를 설정할 수 있고(subscribeOn), 중간에서 실행 스레드를 변경할 수 있다.(publishOn)
        log.info("start main");
        Flux.create(sink -> {
            for (var i = 0; i < 5; i++) {
                log.info("next: {}", i);
                sink.next(i);
            }
        })
        // Scheduler 를 인자로 받음
        // publishOn 이후에 추가되는 연산자들의 실행 스레드에 영향을 줌
        // 그 이후 다른 publishOn 이 적용되면 추가된 Scheduler 로 실행 스레드 변경
        // 스레드풀 중 하나의 스레드만 지속적으로 사용
        .publishOn(
            Schedulers.single()
        ).doOnNext(item -> {
            log.info("doOnNext: {}", item);
        }).publishOn(
            Schedulers.boundedElastic()
        ).doOnNext(item -> {
            log.info("doOnNext2: {}", item);
        }).subscribe(value -> {
            log.info("value: " + value);
        });
        Thread.sleep(1000);
        log.info("end main");
        //19:26 [main] - next: 0
        //19:26 [main] - next: 1
        //19:26 [main] - next: 2
        //19:26 [single-1] - doOnNext: 0
        //19:26 [main] - next: 3
        //19:26 [main] - next: 4
        //19:26 [single-1] - doOnNext: 1
        //19:26 [single-1] - doOnNext: 2
        //19:26 [boundedElastic-1] - doOnNext2: 0
        //19:26 [single-1] - doOnNext: 3
        //19:26 [single-1] - doOnNext: 4
        //19:26 [boundedElastic-1] - value: 0
        //19:26 [boundedElastic-1] - doOnNext2: 1
        //19:26 [boundedElastic-1] - value: 1
        //19:26 [boundedElastic-1] - doOnNext2: 2
        //19:26 [boundedElastic-1] - value: 2
        //19:26 [boundedElastic-1] - doOnNext2: 3
        //19:26 [boundedElastic-1] - value: 3
        //19:26 [boundedElastic-1] - doOnNext2: 4
        //19:26 [boundedElastic-1] - value: 4
        //19:27 [main] - end main
    }
}
