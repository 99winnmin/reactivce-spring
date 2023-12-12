package com.example.reactor.subscribe;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class SubscribeBufferExample {
    public static void main(String[] args) {
        log.info("start main");

        // buffer(N) 호출시 N개씩 묶어서 전달
        var subscriber = new BaseSubscriber<List<Integer>>() {
            private Integer count = 0;

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            @Override
            protected void hookOnNext(List<Integer> value) {
                log.info("value: " + value);
                if (++count == 2) cancel(); // 2세트만 전달
            }

            @Override
            protected void hookOnComplete() {
                log.info("complete");
            }
        };

        Flux.fromStream(IntStream.range(0, 10).boxed())
            // buffer(3) 호출 후 request(2) 를 하는 경우 3개가 담긴 List 2개가 subscriber 로 전달
            // 즉 6개의 데이터가 전달되고 종료
            .buffer(3)
            .subscribe(subscriber);
        //59:54 [main] - value: [0, 1, 2]
        //59:54 [main] - value: [3, 4, 5]
        //59:54 [main] - end main

        log.info("end main");
    }
}
