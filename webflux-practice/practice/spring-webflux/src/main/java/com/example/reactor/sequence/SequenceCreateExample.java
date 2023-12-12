package com.example.reactor.sequence;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class SequenceCreateExample {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        //     public static <T> Flux<T> create(
        //     Consumer<? super FluxSink<T>> emitter)
        // 비동기적으로 Flux 를 생성
        // FluxSink 를 통해서 데이터를 전달
            // 명시적으로 next, error, complete 를 호출 가능
            // SynchronousSink 와 다르게 여러 번 next 가능
            // 여러 thread 에서 동시에 호출 가능
        Flux.create(sink -> {
            var task1 = CompletableFuture.runAsync(() -> {
                for (int i = 0; i < 5; i++) {
                    log.info("task1: " + i);
                    sink.next(i);
                }
            });

            var task2 = CompletableFuture.runAsync(() -> {
                for (int i = 5; i < 10; i++) {
                    log.info("task2: " + i);
                    sink.next(i);
                }
            });

            CompletableFuture.allOf(task1, task2)
                .thenRun(sink::complete);
        }).subscribe(value -> {
            log.info("value: " + value);
        }, error -> {
            log.error("error: " + error);
        }, () -> {
            log.info("complete");
        });

        Thread.sleep(1000);
        //25:25 [ForkJoinPool.commonPool-worker-1] - task1: 0
        //25:25 [ForkJoinPool.commonPool-worker-1] - value: 0
        //25:25 [ForkJoinPool.commonPool-worker-1] - task1: 1
        //25:25 [ForkJoinPool.commonPool-worker-2] - task2: 5
        //25:25 [ForkJoinPool.commonPool-worker-1] - value: 1
        //25:25 [ForkJoinPool.commonPool-worker-1] - task1: 2
        //25:25 [ForkJoinPool.commonPool-worker-1] - value: 2
        //25:25 [ForkJoinPool.commonPool-worker-1] - value: 5
        //25:25 [ForkJoinPool.commonPool-worker-1] - task1: 3
        //25:25 [ForkJoinPool.commonPool-worker-2] - task2: 6
        //25:25 [ForkJoinPool.commonPool-worker-1] - value: 3
        //25:25 [ForkJoinPool.commonPool-worker-2] - task2: 7
        //25:25 [ForkJoinPool.commonPool-worker-1] - value: 6
        //25:25 [ForkJoinPool.commonPool-worker-2] - task2: 8
        //25:25 [ForkJoinPool.commonPool-worker-2] - task2: 9
    }
}
