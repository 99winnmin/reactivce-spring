package com.example.reactor;

import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class FluxSimpleSubscribeOnExample {

    @SneakyThrows
    public static void main(String[] args) {

        log.info("start main");
        getItems()
            .map(i -> {
                log.info("map: {}", i);
                return i;
            })
            .subscribeOn(Schedulers.single())
            .subscribe(new SimpleSubscriber<>(Integer.MAX_VALUE));
        log.info("end main");
        Thread.sleep(1000);
    }

    private static Flux<Integer> getItems() {
        return Flux.fromIterable(List.of(1,2,3,4,5));
    }

    // 출력
    //51:30 [main] - start main
    //51:30 [main] - Using Slf4j logging framework
    //51:30 [main] - subscribe
    //51:30 [main] - request: 2147483647
    //51:30 [main] - end main
    //51:30 [single-1] - map: 1
    //51:30 [single-1] - item: 1
    //51:30 [single-1] - map: 2
    //51:30 [single-1] - item: 2
    //51:30 [single-1] - map: 3
    //51:30 [single-1] - item: 3
    //51:30 [single-1] - map: 4
    //51:30 [single-1] - item: 4
    //51:30 [single-1] - map: 5
    //51:30 [single-1] - item: 5
    //51:31 [single-1] - complete
}
