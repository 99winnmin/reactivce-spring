package com.example.rxjava;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableExample {

    public static void main(String[] args) {
        getCompletion() // 사건이 발생했음을 알림
            .subscribe(new SimpleCompletableObserver());
        //49:49 [main] - onSubscribe
        //49:51 [main] - onComplete

        getError()
            .subscribe(new SimpleCompletableObserver());
        //51:27 [main] - onSubscribe
        //51:28 [main] - onError: error
    }

    private static Completable getCompletion() {
        return Completable.create(c -> {
            Thread.sleep(1000);
            c.onComplete();
        });
    }

    private static Completable getError() {
        return Completable.create(c -> {
            Thread.sleep(1000);
            c.onError(
                new RuntimeException("error")
            );
        });
    }

}
