package com.example.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleSingleObserver implements SingleObserver {
    private Disposable disposable;

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        log.info("onSubscribe");
        this.disposable = disposable;
    }

    @Override
    public void onSuccess(@NonNull Object o) {
        log.info("onSuccess: {}", o);
    }


    @Override
    public void onError(@NonNull Throwable throwable) {
        log.info("onError: {}", throwable.getMessage());
    }

}
