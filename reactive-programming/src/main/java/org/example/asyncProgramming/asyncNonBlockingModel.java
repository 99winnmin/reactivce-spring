package org.example.asyncProgramming;

import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class asyncNonBlockingModel {
  private final static Logger log = Logger.getGlobal();

  public static void main(String[] args) {
    // 비동기적인 호출이 이뤄짐 : main 은 getResult 의 결과에 관심이 없음
    // Non-blocking : getResult 를 호출한 후, getResult가 완료되지 않더라도 main 은 본인의 일을 끝냄
    log.info("start main");
    getResult(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) {
        var nextValue = integer + 1;
        assert nextValue == 1;
      }
    });
    log.info("end main");

  }

  public static void getResult(Consumer<Integer> callback) {
    var executor = Executors.newSingleThreadExecutor();
    try {
      executor.submit(new Runnable() {
        @Override
        public void run() {
          log.info("start getResult");
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          var result = 0;
          try {
            callback.accept(result);
          } finally {
            log.info("end getResult");
          }
        }
      });
    } finally {
      executor.shutdown();
    }
  }
}
