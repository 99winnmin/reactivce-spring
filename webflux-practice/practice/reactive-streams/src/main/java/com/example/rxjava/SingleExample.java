package com.example.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingleExample {

    public static void main(String[] args) {
        getItems()
            .subscribe(new SimpleSingleObserver());
        //38:01 [main] - onSubscribe
        //38:01 [main] - onSuccess: 10

        getError() // Single 은 무조건 값이 있어야함
            .subscribe(new SimpleSingleObserver());
        //38:46 [main] - onSubscribe
        //38:46 [main] - onError: onSuccess called with a null value. Null values are generally not allowed in 3.x operators and sources.
    }

    private static Single<Integer> getItems() {
        return Single.create(c -> {
            c.onSuccess(10);
            c.onSuccess(2);
            c.onSuccess(3);
            c.onSuccess(4);
            c.onSuccess(5);
        });
    }

    private static Single<Integer> getError() {
        return Single.create(c -> {
            c.onSuccess(null);
        });
    }
}
