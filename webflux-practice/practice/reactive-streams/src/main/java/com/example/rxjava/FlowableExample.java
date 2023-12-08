package com.example.rxjava;

import com.example.reactor.ContinuousRequestSubscriber;
import com.example.reactor.SimpleSubscriber;
import io.reactivex.rxjava3.core.Flowable;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FlowableExample {

    public static void main(String[] args) {
        log.info("start main");
        getItems() // Flux 와 유사한 동작 구조
            .subscribe(new SimpleSubscriber<>(Integer.MAX_VALUE));
        //24:38 [main] - subscribe
        //24:38 [main] - request: 2147483647
        //24:38 [main] - item: 1
        //24:38 [main] - item: 2
        //24:38 [main] - item: 3
        //24:39 [main] - item: 4
        //24:39 [main] - item: 5
        //24:39 [main] - complete
        getItems() // backpressure 를 지원하는 Flowable
            .subscribe(new ContinuousRequestSubscriber<>());
        //24:39 [main] - subscribe
        //24:39 [main] - request: 9223372036854775807
        //24:39 [main] - item: 1
        //24:40 [main] - request: 1
        //24:40 [main] - item: 2
        //24:41 [main] - request: 1
        //24:41 [main] - item: 3
        //24:42 [main] - request: 1
        //24:42 [main] - item: 4
        //24:43 [main] - request: 1
        //24:43 [main] - item: 5
        //24:44 [main] - request: 1
        log.info("end main");
    }
    private static Flowable<Integer> getItems() {
        return Flowable.fromIterable(List.of(1,2,3,4,5));
    }

}
