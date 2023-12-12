package com.example.reactor.subscribe;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class SubscribeBaseSubscriberExample {
    public static void main(String[] args) {
        log.info("start main");
        // reactor 에서 BaseSubscriber 를 제공
        // onNext, onError, onComplete 를 직접 호출하는 대신
        // hookOnNext, hookOnError, hookOnComplete 를 오버라이딩하여 구현
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

        Flux.fromIterable(List.of(1, 2, 3, 4, 5))
            .subscribe(subscriber);

        // 중요 : subscriber 외부에서 request 와 cancel 을 호출할 수 있음
        // 기본적으로 unbounded request 이기 때문에 모든 값을 전달받음

        // subscriber.request(1);
        // subscriber.cancel();
        log.info("end main");
    }
}
