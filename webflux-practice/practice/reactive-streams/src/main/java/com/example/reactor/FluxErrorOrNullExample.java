package com.example.reactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxErrorOrNullExample {

    public static void main(String[] args) {
        log.info("start main");
        getErrorItems()
            .subscribe(new SimpleSubscriber<>(Integer.MAX_VALUE));
        getNoItems()
            .subscribe(new SimpleSubscriber<>(Integer.MAX_VALUE));
        log.info("end main");
    }

    private static Flux<Integer> getErrorItems() {
        return Flux.create(fluxSink -> {
            fluxSink.next(0);
            fluxSink.next(1);
            var error = new RuntimeException("error");
            fluxSink.error(error);
        });
    }

    private static Flux<Integer> getNoItems() {
        return Flux.create(fluxSink -> {
            fluxSink.complete();
        });
    }
}
