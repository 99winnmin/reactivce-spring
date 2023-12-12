package com.example.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class OnErrorReturnAfterExecuteExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.just(1)
            // 고정된 값을 넘기기 위해 함수를 실행하면 문제가 발생할 수 있음
                // 에러가 발생하지 않더라도 무조건 함수를 실행한 후 값을 사용
            .onErrorReturn(shouldDoOnError())
            .subscribe(value -> {
                log.info("value: " + value);
            });
        log.info("end main");
        //36:10 [main] - start main
        //36:10 [main] - shouldDoOnError
        //36:10 [main] - value: 1
        //36:10 [main] - end main
    }

    private static int shouldDoOnError() {
        log.info("shouldDoOnError");
        return 0;
    }
}
