package com.example.error;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Slf4j
public class OnErrorMapExample {
    private static class CustomBusinessException
        extends RuntimeException {
        public CustomBusinessException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        log.info("start main");
        Flux.error(new IOException("fail to read file"))
            // onError 이벤트를 처리하기 위해 다른 에러로 변환
            // 다른 이벤트로 변환하여 저수준의 에러를 고수준의 에러, 비지니스 로직과 관련된 에러로 변환 가능
            .onErrorMap(e -> new CustomBusinessException("custom"))
            .subscribe(value -> {
                    log.info("value: " + value);
                }, e -> { // 변환만 하기 때문에 추가적인 에러 핸들링은 여전히 필요
                    log.info("error: " + e);
                }
            );
        log.info("end main");
        //42:17 [main] - start main
        //42:17 [main] - error: com.example.error.OnErrorMapExample$CustomBusinessException: custom
        //42:17 [main] - end main
    }
}
