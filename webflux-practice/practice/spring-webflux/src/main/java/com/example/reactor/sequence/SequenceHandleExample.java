package com.example.reactor.sequence;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

@Slf4j
public class SequenceHandleExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        // 독립적으로 sequence 를 생성할 수는 없고 존재하는 source 에 연결
//        public final <R> Flux<R> handle(BiConsumer<? super T, SynchronousSink<R>> handler)
        // handler
            // 첫번째 인자로 source 의 item 이 제공
            // 두번째 인자로 SynchronousSink 를 제공
            // sink 의 next 를 이용해서 현재의 주어진 item 을 전달할지 말지 결정
        // 일종의 interceptor 로 source 의 item 을 필터하거나 변경할 수 있음
        Flux.fromStream(IntStream.range(0, 10).boxed())
        .handle((value, sink) -> { // handle 을 통해서 value, sink 가 전달
            sink.complete();
            if (value % 2 == 0) {
                // next 를 호출하여 값을 필터하거나 변경할 수 있꼬
                // complete, error 를 더 일찍 전달 가능
                sink.next(value);
            }
        }).subscribe(value -> {
            log.info("value: " + value);
        }, error -> {
            log.error("error: " + error);
        }, () -> {
            log.info("complete");
        });
        log.info("end main");
    }
}
