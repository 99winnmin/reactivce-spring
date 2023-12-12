package com.example.reactor.subscribe;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

@Slf4j
public class SubscribeLimitRequestExample {
    public static void main(String[] args) {
        log.info("start main");

        var subscriber = new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                log.info("value: " + value);
            }

            @Override
            protected void hookOnComplete() {
                log.info("complete");
            }
        };

        // Subscriber 외부에서 연산자를 통해 최대 개수를 제한
        Flux.fromStream(IntStream.range(0, 10).boxed())
            // limitRequest 가 true 인 경우, 정확히 n개 만큼 요청 후 complete 이벤트를 전달
            // BaseSubscriber 의 기본전략이 unbounded 이지만 take 로 인해서 5개 전달 후 complete 이벤트
            .take(5, true)
            .subscribe(subscriber);
        //03:19 [main] - value: 0
        //03:19 [main] - value: 1
        //03:19 [main] - value: 2
        //03:19 [main] - value: 3
        //03:19 [main] - value: 4
        //03:19 [main] - complete
        //03:19 [main] - end main
        log.info("end main");
    }
}
