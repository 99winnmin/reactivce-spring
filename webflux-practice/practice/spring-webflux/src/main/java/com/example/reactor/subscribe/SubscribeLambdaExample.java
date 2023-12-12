package com.example.reactor.subscribe;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import java.util.List;

@Slf4j
public class SubscribeLambdaExample {
    public static void main(String[] args) {
        log.info("start main");
        // 람다식으로 변경하여 처리가능
        // subscription 을 받을 수 없기 때문에 backpressure 를 이용 불가능
        // 따라서 backpressure 가 필요한 경우 Subscriber 기반의 subscribe 권장
        Flux.fromIterable(List.of(1, 2, 3, 4, 5))
            .subscribe(value -> {
                log.info("value: " + value);
            }, error -> {
                log.error("error: " + error);
            }, () -> {
                log.info("complete");
            }, Context.empty());
        log.info("end main");
    }
}