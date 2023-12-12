package com.example.reactor.context;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

@Slf4j
public class ContextReadFromSinkExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        var initialContext = Context.of("name", "ryu");

        Flux.create(sink -> {
            // source 에 sink 가 있다면 contextView 로 접근 가능
            var name = sink.contextView().get("name");
            log.info("name in create: " + name);
            sink.next(1);
        }).contextWrite(context ->
            context.put("name", "seungmin")
        ).subscribe(null, null, null, initialContext);

        Thread.sleep(1000);
        log.info("end main");
        //55:54.178 [main] - start main
        //55:54.211 [main] - name in create: seungmin
        //55:55.216 [main] - end main
    }
}