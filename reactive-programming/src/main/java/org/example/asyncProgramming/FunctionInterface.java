package org.example.asyncProgramming;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FunctionInterface {
  private final static Logger log = Logger.getGlobal();

  public static void main(String[] args) {
    log.setLevel(Level.INFO);
    var consumer = getConsumer();
    consumer.accept(1);

    var consumerLambda = getConsumerLambda();
    consumerLambda.accept(1);

    // 1급 객체는 인자로 넘기는 것 또한 가능
    handleConsumer(consumer);
  }

  public static Consumer<Integer> getConsumer() {
    // Consumer 는 1급 객체이기 때문에 반환값으로 사용 가능
    Consumer<Integer> returnValue = new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) {
        log.info("value in interface : " + integer);
      }
    };
    return returnValue;
  }

  public static Consumer<Integer> getConsumerLambda() {
    // accept 안으로 들어감
    return (Integer integer) -> log.info("value in lambda : " + integer);
  }

  public static void handleConsumer(Consumer<Integer> consumer) {
    log.info("handleConsumer");
    consumer.accept(1);
  }
}
