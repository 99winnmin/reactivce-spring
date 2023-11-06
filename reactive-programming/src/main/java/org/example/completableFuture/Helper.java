package org.example.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

// 예제 코드를 위한 Helper 클래스
public class Helper {
  private final static Logger log = Logger.getGlobal();


  // 1을 반환하는 완료된 CompletionFuture를 반환
  public static CompletionStage<Integer> finishedStage() throws InterruptedException {
    var future = CompletableFuture.supplyAsync(() -> {
      log.info("supplyAsync");
      return 1;
    });
    Thread.sleep(1000);
    return future;
  }

  // 1초를 대기하고 1을 반환하는 CompletionFuture를 반환
  public static CompletionStage<Integer> runningStage() throws InterruptedException {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(1000);
        log.info("running");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return 1;
    });
  }

  public static CompletionStage<Integer> addOne(int value) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return value + 1;
    });
  }

  public static CompletionStage<String> addPrefix(int value) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "result: " + value;
    });
  }

  public static CompletableFuture<Integer> waitAndReturn(int time, int value) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(time);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return value;
    });
  }

}
