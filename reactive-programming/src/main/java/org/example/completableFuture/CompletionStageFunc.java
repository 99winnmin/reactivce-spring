package org.example.completableFuture;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
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

  public static void thenAcceptRunning() throws InterruptedException {
    log.info("start main");
    CompletionStage<Integer> stage = Helper.runningStage();
    stage.thenAccept(result -> {
      log.info(result + " in thenAccept");
    }).thenAccept(result -> {
      log.info(result + " in thenAccept2");
    });
    log.info("end main");

    Thread.sleep(1000); // 데몬 스레드라서 main 스레드가 종료되면 바로 종료되기 때문에 1초 대기
  }

  public static void thenAcceptAsyncRunning() throws InterruptedException {
    log.info("start main");
    CompletionStage<Integer> stage = Helper.runningStage();
    stage.thenAcceptAsync(result -> {
      log.info(result + " in thenAccept");
    }).thenAcceptAsync(result -> {
      log.info(result + " in thenAccept2");
    });
    log.info("end main");

    Thread.sleep(1000); // 데몬 스레드라서 main 스레드가 종료되면 바로 종료되기 때문에 1초 대기
  }

  public static void thenAcceptAsyncFixedThread() throws InterruptedException {
    var single = Executors.newSingleThreadExecutor();
    var fixed = Executors.newFixedThreadPool(2);

    log.info("start main");
    CompletionStage<Integer> stage = Helper.finishedStage();
    stage.thenAcceptAsync(result -> {
      log.info(result + " in thenAccept");
    }, fixed).thenAcceptAsync(result -> {
      log.info(result + " in thenAccept2");
    }, single);
    log.info("end main");

    Thread.sleep(1000); // 데몬 스레드라서 main 스레드가 종료되면 바로 종료되기 때문에 1초 대기
    single.shutdown();
    fixed.shutdown();
  }


  /*
  thenApply[Async]
  - Function 을 파라미터로 받음
  - 이전 task 로부터 T 타입의 값을 받아서 가공하고 U 타입의 값을 반환
  - 값을 변형해서 전달해야하는 경우 유용
   */
  public static void thenApplyAsync() throws InterruptedException {
    CompletionStage<Integer> stage = Helper.finishedStage();
    stage.thenApplyAsync(value -> {
      var next = value + 1;
      log.info(next + " in thenApply");
      return next;
    }).thenApplyAsync(value -> {
      log.info(value + " in thenApply2");
      return "result: " + value;
    }).thenApplyAsync(value -> {
      var next = value.equals("result: 2");
      return next;
    }).thenAcceptAsync(value -> {
      log.info(value + " in thenAccept");
    });

    Thread.sleep(100);
  }

  /*
  thenCompose[Async]
  - Function 을 파라미터로 받음
  - 이전 task 로부터 T 타입의 값을 받아서 가공해서 CompletionStage<U> 타입의 값을 반환
  - 반환한 CompletionStage가 done 상태가 되면 값을 다음 task 에게 전달
  - 다른 future 를 반환해야하는 경우 유용
   */
  public static void thenComposeAsync() throws InterruptedException {
    CompletionStage<Integer> stage = Helper.finishedStage();
    stage.thenComposeAsync(value -> {
      log.info(value + " in thenCompose");
      return Helper.addOne(value);
    }).thenComposeAsync(value -> {
      log.info(value + " in thenCompose2");
      return Helper.addPrefix(value);
    }).thenAcceptAsync(value -> {
      log.info(value + " in thenAcceptAsync");
    });

    Thread.sleep(10000);
  }


  /*
  thenRun[Async]
  - Runnable 을 파라미터로 받음
  - 이전 task 로부터 값을 받지 않고, 다음 task 에게 값을 전달하지 않음, null이 전달됨
  - future 가 완료되었다는 이벤트를 기록할 때 유용
   */
  public static void thenRunAsync() throws InterruptedException {
    CompletionStage<Integer> stage = Helper.finishedStage();
    stage.thenRun(() -> {
      log.info("in thenRun");
    }).thenRun(() -> {
      log.info("in thenRun2");
    }).thenAcceptAsync(value -> {
      log.info(value + " in thenAcceptAsync");
    });

    Thread.sleep(1000);
  }

  /*
  exceptionally
  - Function을 파라미터로 받음
  - 이전 task에서 발생한 exception 을 받아서 처리하고 값을 반환
  - future 파이프에서 발생한 에러를 처리할 때 유용
   */
  public static void exceptionally() throws InterruptedException {
    CompletionStage<Integer> stage = Helper.finishedStage();
    stage.thenApplyAsync(value -> {
      log.info(value + " in thenApply");
      return value / 0;
    }).exceptionally(throwable -> {
      log.info(throwable.getMessage());
      return -1;
    }).thenAcceptAsync(value -> {
      log.info(value + " in thenAcceptAsync");
    });

    Thread.sleep(1000);
  }

  public static void main(String[] args) throws InterruptedException {
//    thenAccept();
//    thenAcceptAsync();
//    thenAcceptRunning();
//    thenAcceptAsyncRunning();

    // CompletionStage
    // then*
      // stage 상태에서 isDone=true 라면 thenAccept 는 caller(main) 스레드에서 실행, isDone=false 라면 thenAccept 는 callee 스레드에서 action 실행
      // 다소 위험한 경향이 있음
    // then*Async
      // stage 상태와 상관없이 thread pool에 있는 스레드에서 action 실행

    // 특정 상황은 명시적으로 다른 스레드에서 실행하고 싶다면?
//    thenAcceptAsyncFixedThread();

//    thenApplyAsync();
//    thenComposeAsync();
//    thenRunAsync();
    exceptionally();
  }

}
