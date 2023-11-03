package org.example.completableFuture;

import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

public class CompletionStageFunc {
  private final static Logger log = Logger.getGlobal();

  // thenApply, thenAccept, thenRun, thenCompose 와 같은 순으로 진행됌(파이프 연결)
  // 50개에 가까운 연산자들을 활용하여 비동기 task들을 실행하고 값을 변형하는 등 chaining 을 이용한 조합 가능
  // 에러를 처리하기 위한 콜백 제공

  /*
  ForkJoinPool - thread pool
  - CompletableFuture 는 내부적으로 비동기 함수들을 실행하기 위해 ForkJoinPool을 사용
  - ForkJoinPool 의 기본 size = 할당된 cpu 코어 수 - 1
  - 데몬 스레드임. main 스레드가 종료되면 바로 종료
  - ForkJoinPool 은 work-stealing 알고리즘을 사용
    - task 를 fork 를 통해서 subtask 로 나누고 pool에서 steal work 알고리즘을 이용해서 균등 처리
    - join 을 통해서 결과 생성
   */

  // CompletionStage Operators
  /*
  thenAccept[Async]
  - 결과를 소비하는 Consumer를 인자로 받음
  - 이전 task 로부터 값을 받지만 값을 넘기지 않음, 다음 task에게 null을 전달
  - 값을 받아서 action만 수행하는 경우 유용
   */
  // thenAccept

  public static void main(String[] args) throws InterruptedException {
//    thenAccept();
    thenAcceptAsync();
  }

  public static void thenAccept() throws InterruptedException {
    // done 상태에서 thenAccept 는 caller(main) 스레드에서 실행
    // done 상태의 completionStage 에 thenAccept를 사용하는 경우, caller 스레드를 block 할 수 있음
    log.info("start main");
    CompletionStage<Integer> stage = Helper.finishedStage();
    stage.thenAccept(result -> {
      log.info(result + " in thenAccept");
    }).thenAccept(result -> {
      log.info(result + " in thenAccept2");
    });
    log.info("end main");

    Thread.sleep(1000); // 데몬 스레드라서 main 스레드가 종료되면 바로 종료되기 때문에 1초 대기
  }

  public static void thenAcceptAsync() throws InterruptedException {
    log.info("start main");
    CompletionStage<Integer> stage = Helper.finishedStage();
    stage.thenAcceptAsync(result -> {
      log.info(result + " in thenAccept");
    }).thenAcceptAsync(result -> {
      log.info(result + " in thenAccept2");
    });
    log.info("end main");

    Thread.sleep(1000); // 데몬 스레드라서 main 스레드가 종료되면 바로 종료되기 때문에 1초 대기
  }
}
