package com.example.rxjava;

import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObservableExample {

    public static void main(String[] args) {
        getItems()
            .subscribe(new SimpleObserver());
        //34:04 [main] - onSubscribe
        //34:04 [main] - onNext: 1
        //34:04 [main] - onNext: 2
        //34:04 [main] - onNext: 3
        //34:04 [main] - onNext: 4
        //34:04 [main] - onNext: 5
        //34:04 [main] - onComplete
    }

    private static Observable<Integer> getItems() {
        return Observable.fromIterable(List.of(1,2,3,4,5));
    }
}
