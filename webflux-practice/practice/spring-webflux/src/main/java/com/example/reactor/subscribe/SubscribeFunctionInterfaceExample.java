package com.example.reactor.subscribe;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class SubscribeFunctionInterfaceExample {
    // 함수형 인터페이스를 subscribe 에 제공
    // 각각의 consumer 는 null 가능
    public static void main(String[] args) {
        log.info("start main");
        Flux.fromIterable(List.of(1, 2, 3, 4, 5))
            .subscribe(new Consumer<Integer>() { // 값을 인자로 받아야하기 때문에 Consumer 함수형 인터페이스 구현
                @Override
                public void accept(Integer integer) {
                    log.info("value: " + integer);
                }
            }, new Consumer<Throwable>() { // 에러를 인자로 받아야 하기 때문에 Consumer 구현
                @Override
                public void accept(Throwable throwable) {
                    log.error("error: " + throwable);
                }
            }, new Runnable() { // 받을 인자가 없기 때문에 runnable 구현
                @Override
                public void run() {
                    log.info("complete");
                }
            }, Context.empty());
        log.info("end main");
    }
}
