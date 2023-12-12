package com.example.reactor.subscribe;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class SubscribeEmptyExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.fromIterable(List.of(1, 2, 3, 4, 5))
            .doOnNext(value -> {
                log.info("value: " + value);
            })
            .subscribe();
        log.info("end main");
        // Publisher 에서 아이템을 만드는 것이 중요한 경우
        // 별도의 consume 을 하지 않고 결과를 확인하기 위해 doOnNext 를 이용
        // doOnNext : 파이프라인에 영향을 주지 않고 지나가는 값을 확인
        //44:15 [main] - value: 1
        //44:15 [main] - value: 2
        //44:15 [main] - value: 3
        //44:15 [main] - value: 4
        //44:15 [main] - value: 5
        //44:15 [main] - end main
    }
}
