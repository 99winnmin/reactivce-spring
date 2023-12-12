package com.example.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ErrorConsumerExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.error(new RuntimeException("error"))
            .subscribe(value -> {
                log.info("value: " + value);
            }, error -> { // subscribe()의 두 번째 인자로 error consumer를 넘길 수 있음
                // error consumer 는 에러를 얻고 action 을 수행함
                // but 이어서 처리하기는 힘듬
                log.info("error: " + error);
            });
        log.info("end main");
        //33:28 [main] - start main
        //33:28 [main] - Using Slf4j logging framework
        //33:28 [main] - error: java.lang.RuntimeException: error
        //33:28 [main] - end main
    }
}
