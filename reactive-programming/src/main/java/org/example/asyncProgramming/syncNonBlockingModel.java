package org.example.asyncProgramming;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class syncNonBlockingModel {
  private final static Logger log = Logger.getGlobal();

  public static void main(String[] args) throws Exception {
    // Non-blocking : getResult 를 호출한 후, getResult가 완료되지 않더라도 main 은 본인의 일을 할 수 있음
    // main 과 getResult 가 각자의 일을함, blocking 이면 main이 다른일을 못
    log.info("start main");
    var count = 1;
    Future<Integer> result = getResult();
    while (!result.isDone()) {
      log.info("waiting for result, count : " +count++);
      Thread.sleep(100);
    }
    var nextValue = result.get() + 1;
    assert nextValue == 1;
    log.info("end main");
  }

  public static Future<Integer> getResult() {
    var executor = Executors.newSingleThreadExecutor();
    try {
      return executor.submit(new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {
          log.info("start getResult");
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          var result = 0;
          try {
            return result;
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
