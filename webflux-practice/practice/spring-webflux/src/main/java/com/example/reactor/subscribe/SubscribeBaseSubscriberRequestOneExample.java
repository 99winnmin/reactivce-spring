package com.example.reactor.subscribe;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class SubscribeBaseSubscriberRequestOneExample {
    public static void main(String[] args) {
        log.info("start main");

        var subscriber = new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }

            // request(1) 을 통해 1개씩만 전달받음
            // onNext 를 통해 1개씩 전달받고 cancel 을 통해 종료
            // BaseSubscriber 를 이용하는 장점
            @Override
            protected void hookOnNext(Integer value) {
                log.info("value: " + value);
                cancel();
            }

            @Override
            protected void hookOnComplete() {
                log.info("complete");
            }
        };

        Flux.fromIterable(List.of(1, 2, 3, 4, 5))
            .subscribe(subscriber);

        log.info("end main");
    }
}
