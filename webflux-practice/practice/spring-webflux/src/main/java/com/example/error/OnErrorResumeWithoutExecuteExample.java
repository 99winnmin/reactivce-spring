package com.example.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class OnErrorResumeWithoutExecuteExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.just(1)
            // onError 이벤트를 처리하기 위해 publisher 를 반환하는 추상 함수를 실행
            // 해당 publisher 의 onNext, onError, onComplete 이벤트를 아래로 전달
            // error 이벤트가 발생한 상황에서만 apply 를 실행
                // Mono.just, Flux.just 와 함께 사용한다면?
                // error 이벤트가 발생한 상황에만 apply 를 실행하기 때문에 불필요하게 shouldDoOnError() 가 실행되지 않음
                // publisher 를 받기 때문에 Flux, Mono 모두 가능
            .onErrorResume(e ->
                Mono.just(shouldDoOnError()))
            .subscribe(value -> {
                log.info("value: " + value);
            });
        log.info("end main");
        //38:18 [main] - start main
        //38:18 [main] - value: 1
        //38:18 [main] - end main
    }

    private static int shouldDoOnError() {
        log.info("shouldDoOnError");
        return 0;
    }
}
