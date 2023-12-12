package com.example.reactor.util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class MapExample {
    public static void main(String[] args) {
        log.info("start main");
        Flux.range(1, 5)
            // onNext 이벤트를 받아서 값을 변경하고 아래로 전달
            // null 전달하면 에러임, null 은 무시하고 싶으면 mapNotNull 사용
            .map(value -> value * 2)
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribe();

        Flux.range(1, 5)
            // 변경된 값이 null 인 경우 필터
            .mapNotNull(value -> {
                if (value % 2 == 0) {
                    return value;
                }
                return null;
            })
            .doOnNext(value -> {
                log.info("doOnNext: " + value);
            })
            .subscribe();
        log.info("end main");
        //18:26.197 [main] - doOnNext: 2
        //18:26.198 [main] - doOnNext: 4
        //18:26.198 [main] - doOnNext: 6
        //18:26.198 [main] - doOnNext: 8
        //18:26.198 [main] - doOnNext: 10
        //18:26.198 [main] - doOnNext: 2
        //18:26.198 [main] - doOnNext: 4
        //18:26.198 [main] - end main
    }
}
