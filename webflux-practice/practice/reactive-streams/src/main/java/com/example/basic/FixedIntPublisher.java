package com.example.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class FixedIntPublisher implements Flow.Publisher<FixedIntPublisher.Result> {
  // 고정된 숫자의 integer 를 전달하는 publisher
  // 8개의 integer 를 전달 후 complete
  // iterator 를 생성해서 subscription 을 생성하고 subsriber 에게 전달
  // requestCount 를 세기 위해서 Result 객체 사용
  @Data
  public static class Result {
    private final Integer value;
    private final Integer requestCount;
  }

  // FixedIntPublisher 를 subscribe 하면 onSubscribe 가 호출되고 numbers 를 받을 수 있게됌
  @Override
  public void subscribe(Flow.Subscriber<? super Result> subscriber) {
    var numbers = Collections.synchronizedList(
        new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7))
    );
    Iterator<Integer> iterator = numbers.iterator();
    var subscription = new IntSubscription(subscriber, iterator);
    subscriber.onSubscribe(subscription);
  }

  @RequiredArgsConstructor
  private static class IntSubscription
      implements Flow.Subscription {
    // subscriber 의 onNext 와 subscription 의 request 가 동기적으로 동작하면 안되기 때문에
    // executor 를 이용해서 별도의 스레드에서 실행
    // 요청 횟수를 count에 저장하고 결과에 함께 전달
    // 더 이상 iterator에 값이 없으면, onComplete 호출

    private final Flow.Subscriber<? super Result> subscriber;
    private final Iterator<Integer> numbers;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicInteger count = new AtomicInteger(1);
    private final AtomicBoolean isCompleted = new AtomicBoolean(false);

    @Override
    public void request(long n) {
      // 별로의 싱글 스레드
      executor.submit(() -> {
        for (int i = 0; i < n; i++) {
          if (numbers.hasNext()) {
            // 동기적으로 진행하게 되면 스택이 쌓이게됌
            int number = numbers.next();
            numbers.remove();
            subscriber.onNext(new Result(number, count.get()));
          } else {
            var isChanged = isCompleted.compareAndSet(false, true);
            if (isChanged) {
              executor.shutdown();
              subscriber.onComplete();
              isCompleted.set(true);
            }
            break;
          }
        }
        count.incrementAndGet();
      });
    }

    @Override
    public void cancel() {
      subscriber.onComplete();
    }
  }

}
