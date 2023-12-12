package com.example.reactor.sequence;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class SequenceGenerateExample {
    public static void main(String[] args) {
        log.info("start main");
        // 동기적으로 Flux 를 생성
        //public static <T, S> Flux<T> generate(
        // Callable<S> stateSupplier,
        // BiFunction<S, SynchronousSink<T>, S> generator)
        // stateSupplier: 초기값을 제공하는 Callable
        // generator
            // S: 상태값, 변경된 상태값을 반환, 상태값으로 종료조건을 지정
            // SynchronousSink<T>: 명시적으로 next, error, complete 호출 가능
            // 한번의 generate 에서 next 를 두번 호출하면 에러 발생(한번만 next 호출 가능)
        Flux.generate(
            () -> 0, // 초기값을 0으로 지정
            // state 는 0부터 시작
            // generator 에서 현재 state 를 next 로 전달
            (state, sink) -> {
                sink.next(state);
                if (state == 9) {
                    sink.complete(); // state 가 9가 되면 종료
                }
                return state + 1;
            }
        ).subscribe(value -> {
            log.info("value: " + value);
        }, error -> {
            log.error("error: " + error);
        }, () -> {
            log.info("complete");
        });
        //22:35 [main] - value: 0
        //22:35 [main] - value: 1
        //22:35 [main] - value: 2
        //22:35 [main] - value: 3
        //22:35 [main] - value: 4
        //22:35 [main] - value: 5
        //22:35 [main] - value: 6
        //22:35 [main] - value: 7
        //22:35 [main] - value: 8
        //22:35 [main] - value: 9
        //22:35 [main] - complete
    }
}
