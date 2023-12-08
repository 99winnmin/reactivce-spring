package com.example.reactor;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoToFluxExample {
    public static void main(String[] args) {
        log.info("start main");
        getItems().flux()
                .subscribe(new SimpleSubscriber<>(Integer.MAX_VALUE));
        //18:25 [main] - subscribe
        //18:25 [main] - request: 2147483647
        //18:25 [main] - item: [1, 2, 3, 4, 5]
        //18:25 [main] - complete

        getItems()
                .flatMapMany(value -> Flux.fromIterable(value))
                .subscribe(new SimpleSubscriber<>(Integer.MAX_VALUE));
        //19:20 [main] - subscribe
        //19:20 [main] - request: 2147483647
        //19:20 [main] - item: 1
        //19:20 [main] - item: 2
        //19:20 [main] - item: 3
        //19:20 [main] - item: 4
        //19:20 [main] - item: 5
        //19:20 [main] - complete
        log.info("end main");
    }

    private static Mono<List<Integer>> getItems() {
        return Mono.just(List.of(1,2,3,4,5));
    }
}
