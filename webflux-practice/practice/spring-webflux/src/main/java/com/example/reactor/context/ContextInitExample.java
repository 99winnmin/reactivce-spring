package com.example.reactor.context;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

@Slf4j
public class ContextInitExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        var initialContext = Context.of("name", "승민");

        Flux.just(1)
            .flatMap(v -> ContextLogger.logContext(v, "1"))
            .contextWrite(context ->
                context.put("name", "seungmin"))
            .flatMap(v -> ContextLogger.logContext(v, "2"))
            // subscribe 에 4번째 인자로 초기값 전달 가능
            // 이 경우에도 subscribe 부터 위로 전파
            .subscribe(null, null, null, initialContext);
        log.info("end main");
        //53:43.615 [main] - start main
        //53:43.655 [main] - name: 1, context: Context1{name=seungmin}
        //53:43.655 [main] - name: 2, context: Context1{name=승민}
        //53:43.656 [main] - end main
    }
}
