package org.example.completableFuture;

public class ExecutorServiceEX {
  /*
  execute : Runnable 인터페이스를 구현한 작업을 스레드 풀에서 비동기적으로 실행
  submit : Callable 인터페이스를 구현한 작업을 스레드 풀에서 비동기적으로 실행하고, 해당 작업의 결과를 Future<T> 객체로 반환
  shutdown : ExecutorService 를 종료, 더이상 task 를 받지 않음

  service 생성
  newSingleThreadExecutor : 스레드 1개로 구성된 스레드 풀 생성, 한번에 하나의 작업만 실행
  newFixedThreadPool : 스레드 n개로 구성된 스레드 풀 생성, n개의 작업을 동시에 실행
  newCachedThreadPool : 사용 가능한 스레드가 없다면 새로 생성해서 작업을 처리, 있다면 재사용. 스레드가 일정 시간 사용되지 않으면 회수. 예상 가능한 범위에서만 사용
  newScheduledThreadPool : 스케줄링 기능을 갖춘 고정 크기 쓰레드 풀을 생성. 주기적이거나 지연이 발생하는 작업을 실행
  newWorkStealingPool : work steal 알고리즘을 사용하는 ForkJoinPool을 생성.
   */
}
