package org.example.completableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureFunc {
  // 새로운 스레드를 생성하여 1을 반환
  public static Future<Integer> getFuture() {
    var executor = Executors.newSingleThreadExecutor();

    try {
      return executor.submit(() -> {
        return 1;
      });
    } finally {
      executor.shutdown();
    }
  }

  // 새로운 스레드를 생성하고 1초 대기 후 1을 반환
  public static Future<Integer> getFutureCompleteAfter1s() {
    var executor = Executors.newSingleThreadExecutor();

    try {
      return executor.submit(() -> {
        Thread.sleep(1000);
        return 1;
      });
    } finally {
      executor.shutdown();
    }
  }

  // Future 내부 함수
  /*
  isDone : task 가 완료되었다면 완료, 실패 상관없이 true 반환
  isCancelled : task가 명시적으로 취소된 경우, true 반환
  get : 결과를 구할 때까지 스레드가 계속 block 되어 대기
  get(long timeout, TimeUnit unit) : timeout 시간까지 결과를 기다림
  cancel : task를 취소, 이미 완료된 task or 이미 취소된 상황에 대해서는 false 반환
   */
  public static void main(String[] args)
      throws ExecutionException, InterruptedException, TimeoutException {
//    Future future = FutureFunc.getFuture();
//    assert !future.isDone();
//    assert !future.isCancelled();
//
//    var result = future.get(); // blocking 되기 때문에 위험함
//    assert result.equals(1);
//    assert future.isDone();
//    assert !future.isCancelled();
//
//    Future futureCompleteAfter1s = FutureFunc.getFutureCompleteAfter1s();
//    var result2 = futureCompleteAfter1s.get(1500, TimeUnit.MILLISECONDS);
//    assert result2.equals(1);

    Future future2 = FutureFunc.getFuture();
    var successToCancel = future2.cancel(true);
    assert future2.isDone();
    assert future2.isCancelled();
    assert successToCancel;

    successToCancel = future2.cancel(true); // 이미 위에서 취소했기 때문에 false 반환
//    successToCancel = future2.cancel(false); // false 를 넣을 경우, 시작하지 않은 작업에 대해서만 취소 가능
    assert future2.isDone();
    assert future2.isCancelled();
    assert !successToCancel;

    /*
    Future 인터페이스의 한계
    1. cancel 을 제외하고 외부에서 future를 컨트롤할 수 없음
    2. 반환된 결과를 get()해서 접근하기 때문에 비동기 처리가 어려움
    3. 완료되거나 에러가 발생했는지 구분하기 어려움
     */
  }



}
