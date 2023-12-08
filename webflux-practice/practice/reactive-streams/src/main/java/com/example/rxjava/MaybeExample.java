package com.example.rxjava;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MaybeExample {

    public static void main(String[] args) {
        getItems()
            .subscribe(new SimpleMaybeObserver<>());
        //38:01 [main] - onSubscribe
        //38:01 [main] - onSuccess: 10

        getEmpty() // 값이 없으면 complete 실행
            .subscribe(new SimpleMaybeObserver());
        //45:05 [main] - onSubscribe
        //45:05 [main] - onComplete

        getError() // 에러 날라오면 error 처리
            .subscribe(new SimpleMaybeObserver());
        //45:05 [main] - onSubscribe
        //45:05 [main] - onError: error
    }

    private static Maybe<Integer> getItems() {
        return Maybe.create(c -> {
            c.onSuccess(10);
            c.onSuccess(2);
            c.onSuccess(3);
            c.onSuccess(4);
            c.onSuccess(5);
        });
    }

    private static Maybe<Integer> getEmpty() {
        return Maybe.create(c -> {
            c.onComplete();
        });
    }

    private static Maybe<Integer> getError() {
        return Maybe.create(c -> {
            var error = new RuntimeException("error");
            c.onError(error);
        });
    }
}
