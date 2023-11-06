package org.example.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class CompletableFutureFunc {
  private final static Logger log = Logger.getGlobal();

  /*
  supplyAsync
  - Supplier 를 제공하여 CompletableFuture 를 생성 가능
  - Supplier 의 반환값이 CompletableFuture의 결과로
  - non-blocking 으로 동작
   */
  public static void supplyAsync() throws InterruptedException, ExecutionException {
    var future = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return 1;
    });
    assert !future.isDone(); // non-blocking 이여서 이부분에서는 아직 안끝남

    Thread.sleep(1500);
    assert future.isDone();
    assert future.get().equals(1);
  }

  /*
  runAsync
  - Runnable 을 제공하여 CompletableFuture 를 생성
  - 값 반환 x, null 반환
   */
  public static void runAsync() throws InterruptedException, ExecutionException {
    var future = CompletableFuture.runAsync(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
    assert !future.isDone(); // non-blocking 이여서 이부분에서는 아직 안끝남

    Thread.sleep(1500);
    assert future.isDone();
    assert future.get() == null;
  }

  /*
  complete
  - CompletableFuture 가 완료되지 않았다면 주어진 값으로 채움, 이미 완료된 값은 절대 바뀌지 않음
  - complete 에 의해서 상태가 바뀌었다면 true, 아니라면 false를 반환
   */
  public static void complete() throws InterruptedException, ExecutionException {
    var future = new CompletableFuture<Integer>(); // 기본적으로 모두 다른 스레드에서 실행됌
    assert !future.isDone();

    assert future.complete(1);
    assert future.isDone();
    assert future.get().equals(1);

    assert !future.complete(2); // 이미 완료된 값이기 때문에 false 반환
    assert future.isDone();
    assert future.get().equals(1); // 이미 완료된 것은 값이 바뀌지 않기 때문에 여전히 1임

    future.isCompletedExceptionally(); // 예외가 발생했는지 체크 가능
  }

  /*
  allOf
  - 여러 CompletableFuture 를 모아서 하나의 completeFuture 로 만들어줌
  - 모든 completeFuture 가 완료되면 상태가 done으로 변경
  - void 를 반환하므로 각각의 값에 get 으로 접근 해야함
   */
  public static void allOf() {
    var startTime = System.currentTimeMillis();
    var future1 = Helper.waitAndReturn(6000, 1);
    var future2 = Helper.waitAndReturn(2000, 1);
    var future3 = Helper.waitAndReturn(3000, 1);

    CompletableFuture.allOf(future1, future2, future3)
        .thenAcceptAsync(v -> {
          try {
            log.info("first : " + future1.get());
            log.info("second : " + future2.get());
            log.info("third : " + future3.get());
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }).join();
    var endTime = System.currentTimeMillis();
    log.info("time : " + (endTime - startTime));
  }

  /*
  anyOf
  - 여러 CompletableFuture 를 모아서 하나의 completeFuture 로 만들어줌
  - 주어진 future 중 하나라도 완료되면 상태가 done 으로 변경
  - 제일 먼저 done 상태가 되는 future 의 값을 반환
   */
  public static void anyOf() {
    var startTime = System.currentTimeMillis();
    var future1 = Helper.waitAndReturn(600, 6);
    var future2 = Helper.waitAndReturn(200, 2);
    var future3 = Helper.waitAndReturn(300, 3);

    CompletableFuture.anyOf(future1, future2, future3)
        .thenAcceptAsync(v -> {
          try {
            log.info("start");
            log.info("first : " + v);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }).join();
    var endTime = System.currentTimeMillis();
    log.info("time : " + (endTime - startTime));
  }

  /*
  CompletableFuture 의 한계
  - 지연 로딩 기능을 제공하지 않음
  - CompletableFuture 를 반환하는 함수를 호출시 즉시 작업이 실행됨
  - 지속적으로 생성되는 데이터를 처리하기 어려움(한번에 모아서 데이터를 처리해야함)
  - CompletableFuture 에서 데이터를 반환하고 나면 다시 다른 값을 전달하기 어려움
   */


  public static void main(String[] args) throws ExecutionException, InterruptedException {
//    complete();
//    allOf();
    anyOf();
  }

}
