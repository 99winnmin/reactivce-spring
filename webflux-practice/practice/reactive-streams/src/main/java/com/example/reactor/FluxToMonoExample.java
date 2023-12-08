package com.example.reactor;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FluxToMonoExample {

    public static void main(String[] args) {
        log.info("start main");
        // Flux -> Mono
        Mono.from(getItems()) // Flux pub 중에 첫번째 값만 받고 complete 됌
            .subscribe(
                new SimpleSubscriber<>(Integer.MAX_VALUE)
            );
        //16:59 [main] - subscribe
        //16:59 [main] - request: 2147483647
        //16:59 [main] - item: 1
        //16:59 [main] - complete

        getItems()
            .collectList() // Flux 를 Mono<List> 로 변환
            .subscribe(
                new SimpleSubscriber<>(Integer.MAX_VALUE)
            );
        //16:59 [main] - subscribe
        //16:59 [main] - request: 2147483647
        //16:59 [main] - item: [1, 2, 3, 4, 5]
        //16:59 [main] - complete
        log.info("end main");
    }

    private static Flux<Integer> getItems() {
        return Flux.fromIterable(List.of(1,2,3,4,5));
    }
}
