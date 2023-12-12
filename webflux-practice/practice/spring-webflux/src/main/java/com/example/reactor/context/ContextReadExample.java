package com.example.reactor.context;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ContextReadExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");

        Flux.just(1)
            .flatMap(value -> {
                // defer 와 비슷하지만 아무런 값을 전달하지 않고
                // Consumer 가 아닌 ContextView 를 인자로 받는 Function 을 받음
                // Context 를 인자로 받고 생성된 publisher 의 이벤트를 아래로 전달
                Mono<Integer> integerMono = Mono.deferContextual(contextView -> {
                    // Mono.deferContextual 가 contextView 를 인자로 전달하고
                    // Mono 를 반환값으로 받아서 Mono 를 생성
                    // 이렇게 생성된 Mono 를 flatMap 으로 처리
                    String name = contextView.get("name");
                    log.info("name: " + name);
                    return Mono.just(value);
                });
                return integerMono;
            }).contextWrite(context ->
                context.put("name", "seungmin")
            ).subscribe();

        Thread.sleep(1000);
        log.info("end main");
        //04:43.465 [main] - start main
        //04:43.506 [main] - name: seungmin
        //04:44.512 [main] - end main
    }
}
