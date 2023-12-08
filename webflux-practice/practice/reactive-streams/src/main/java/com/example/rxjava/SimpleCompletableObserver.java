package com.example.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleCompletableObserver implements CompletableObserver {
    private Disposable disposable;

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        log.info("onSubscribe");
        this.disposable = disposable;
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        log.info("onError: {}", throwable.getMessage());
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
    }
}
