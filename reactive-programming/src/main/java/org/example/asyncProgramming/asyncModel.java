package org.example.asyncProgramming;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class asyncModel {
  private final static Logger log = Logger.getGlobal();

  public static void main(String[] args) {
    // 비동기적인 호출이 이뤄짐 : main 은 getResult 의 결과에 관심이 없음
    // callee(getResult)는 결과를 이용해서 callback을 수행
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

  public static void getResult(Consumer<Integer> cb) {
    log.info("start getResult");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    var result = 0;
    cb.accept(result);
    log.info("end getResult");
  }
}
