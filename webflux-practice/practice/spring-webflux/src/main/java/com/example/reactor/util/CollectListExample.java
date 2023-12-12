package com.example.reactor.util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class CollectListExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.range(1, 5)
            // next 이벤트가 전달되면 내부에 item 을 저장
            // complete 이벤트가 전달되면 저장했던 item 들을 list 형태로 만들고 아래에 onNext 발행
            // 즉시 complete 이벤트 발행
            // Flux 를 Mono 로 바꿀때 유용
            .collectList()
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribe();
        log.info("end main");
        //29:46.115 [main] - doOnNext: [1, 2, 3, 4, 5]
        //29:46.115 [main] - end main
    }
}
