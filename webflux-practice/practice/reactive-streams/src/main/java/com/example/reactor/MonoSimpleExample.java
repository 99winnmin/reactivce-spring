package com.example.reactor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoSimpleExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        getItems()
            .subscribe(new SimpleSubscriber<>(Integer.MAX_VALUE));
        log.info("end main");

        Thread.sleep(1000);
    }

    /**
     * 1개의 item 만 전달하기 때문에 next 하나만 실행하면 complete 가 보장
     * 혹은 전달하지 않고 complete 를 하면 값이 없다는 것을 의미
     * 0 or 1
     */
    private static Mono<String> getItems() {
        return Mono.create(monoSink -> {
            monoSink.success();
        });
    }
}
