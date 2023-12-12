package com.example.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ErrorNoHandleExample {
    // 에러 핸들링이 없는 경우
        // source 나 연산자에서 에러가 발생했지만 따로 처리하지 않은 경우
        // 기본적으로 onErrorDropped() 를 호출하여 에러를 출력하고 종료한다.
    public static void main(String[] args) {
        log.info("start main");
        Flux.create(sink -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sink.error(new RuntimeException("error"));
        }).subscribe();
        log.info("end main");
        //32:49 [main] - start main
        //32:49 [main] - Using Slf4j logging framework
        //32:49 [main] - Operator called default onErrorDropped
        //reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.RuntimeException: error
        //Caused by: java.lang.RuntimeException: error
    }
}
