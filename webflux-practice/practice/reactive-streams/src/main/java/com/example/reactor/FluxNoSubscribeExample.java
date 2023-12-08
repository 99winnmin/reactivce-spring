package com.example.reactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxNoSubscribeExample {

    public static void main(String[] args) {
        log.info("start main");
        getItems();
        log.info("end main");
    }

    private static Flux<Integer> getItems() {
        return Flux.create(integerFluxSink -> {
            log.info("create");
            for (int i = 0; i < 5; i++) {
                integerFluxSink.next(i);
            }
            integerFluxSink.complete();
            log.info("complete");
        });
    }
}
