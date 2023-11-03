package org.example.asyncProgramming;

import java.util.logging.Logger;

public class syncModel {
  private final static Logger log = Logger.getGlobal();

  public static void main(String[] args) {
    // 동기적인 호출이 이뤄짐 : main 은 getResult 의 결과에 관심이 있음
    // caller(main)는 결과를 이용해서 다음 로직을 수행
    log.info("start main");
    var result = getResult();
    var nextValue = result + 1;
    assert nextValue == 1;
    log.info("end main");
  }

  public static int getResult() {
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
}
