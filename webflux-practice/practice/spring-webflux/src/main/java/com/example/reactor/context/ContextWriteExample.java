package com.example.reactor.context;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ContextWriteExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        // Context
            // Context 는 파이프라인 내부 어디에서든 접근 가능한 key-value store 이다.
            // 특정 key 의 value 에 접근하고 key 의 value 를 변경할 수 있다.
            // map 과 유사
            // 읽기 전용인 ContextView 와 쓰기를 할 수 있는 Context 로 구분
        Flux.just(1)
            .flatMap(v -> ContextLogger.logContext(v, "1"))
            // ContextWrite
                // Context 를 인자로 받고 Context 를 반환하는 함수형 인터페이스를 제공
                // 이를 통해서 기존의 Context 에 값을 추가하거나 변경, 삭제 가능
                // Context 는 immutable 하기 때문에 새로운 Context 를 반환 -> thread safe 함
            .contextWrite(context ->
                context.put("name", "seungmin"))

            .flatMap(v -> ContextLogger.logContext(v, "2"))
            .contextWrite(context ->
                context.put("name", "hihi"))

            // subscribe 부터 시작하여 점차 위로 올라가며 context write 를 만나면 실행하고
            // 새로운 context 를 생성해서 위에 있는 연산자에 전달
            // subscribe 부터 위로 전파
            .flatMap(v -> ContextLogger.logContext(v, "3"))
            .subscribe();
        log.info("end main");
    }
}
