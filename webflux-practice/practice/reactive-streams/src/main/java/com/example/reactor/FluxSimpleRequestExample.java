package com.example.reactor;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxSimpleRequestExample {

    public static void main(String[] args) {
        getItems()
            .subscribe(new ContinuousRequestSubscriber<>());
    }

    private static Flux<Integer> getItems() {
        return Flux.fromIterable(List.of(1,2,3,4,5));
    }

    // 출력
    //00:32 [main] - subscribe
    //00:32 [main] - request: 9223372036854775807
    //00:32 [main] - item: 1
    //00:33 [main] - request: 1
    //00:33 [main] - item: 2
    //00:34 [main] - request: 1
    //00:34 [main] - item: 3
    //00:35 [main] - request: 1
    //00:35 [main] - item: 4
    //00:36 [main] - request: 1
    //00:36 [main] - item: 5
    //00:37 [main] - request: 1
}
