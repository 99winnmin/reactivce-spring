package com.example.reactor.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class DeferFlatMapExample {
    public static void main(String[] args) {
        log.info("start main");
        Mono.just(1)
            // defer 는 주어진 supplier 를 실행해서 Mono 를 구하고 해당 Mono 로 이벤트 전달
            // v 라는 값이 있을 때 Mono.defer(() -> Mono.just(v)) 를 한다면?
                // 이는 결국 Mono.just(v) 와 동일
            .flatMap(v -> Mono.defer(() -> {
                return Mono.just(v);
            }))

            .flatMap(Mono::just)

            .map(v -> {
                return v;
            })
            // 위 3 과정이 동일함
            .subscribe(n -> {
                log.info("next: {}", n);
            });
        log.info("end main");
        //04:25.438 [main] - start main
        //04:25.475 [main] - next: 1
        //04:25.476 [main] - end main
    }
}
