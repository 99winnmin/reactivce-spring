# 목차
```
Reactive란?
- 무언가를 바꾸거나 예방하기 위해 먼저 행동하기보다는 사건이나 상황에 반응하는
```
## 1. Reactive Programming

## 2. 비동기 Programming
```
동기 : caller 가 callee 의 결과에 관심이 있음
비동기 : caller 가 callee 의 결과에 관심이 없음, callee 가 알아서 처리하고 callback 을 줌
```
```
Blocking
    - caller 는 callee 가 완료될때까지 대기함
    - 제어권을 callee가 가지고 있음
    - caller 와 다른 별로의 thread 가 필요하지 않음(혹은 thread를 추가로 쓸 수 있음)
Non-blocking
    - caller 는 callee를 기다리지 않고 본인의 일을 함
    - 제어권을 caller 가 가지고 있음
    - caller 와 다른 별도의 thread 가 필요함
```

- Blocking 종류
  - blocking 은 thread 가 오랜 시간 일을 하거나 대기하는 경우 발생
  - CPU-bound blocking : 오랜 시간 일을 함
    - thread 가 대부분의 시간 cpu 점유
    - 연산이 많은 경우
    - 추가적인 코어를 투입
  - I/O-bound blocking : 오랜 시간 대기함
    - thread 가 대부분의 시간 대기
    - 파일 읽기/쓰기, network 요청 처리, 요청 전달 등
    - IO-bound non-blocking 으로 해결
- Blocking 전파
  - 하나의 함수에서 여러 함수를 호출하기도 하고, 함수 호출은 중첩적으로 발생
  - callee는 caller 가 되고 다시 다른 callee를 호출
    - blocking 한 함수를 하나라도 호출한다면 caller는 blocking 됨
  - 함수가 non-blocking 하려면 모든 함수가 non-blocking 이어야 함
  - 따라서 I/O bound blocking 또한 발생하면 안됌
<br><br>
### I/O 관점에서 Blocking, Non-Blocking
- Sync Blocking I/O
  - application 은 kernel이 I/O 작업을 완료할때까지 기다림
  - 그 후 결과를 직접 이용해서 이후 본인의 일을 수행
- Sync Non-Blocking I/O
  - X
- Async Blocking
  - application은 kernel에게 주기적으로 I/O 작업이 완료되었는지 확인함
  - 중간중간 본인의 일을 할 수 있고 작업이 완료되면 그때 본인의 일을 수행
- Async Non-Blocking
  - application은 kernel에게 I/O 작업을 요청하고 본인의 일을 수행
  - kernel은 I/O 작업이 완료되면 signal을 보내거나 callback을 호출
    - 원래 스레드로 반환할 수 도 있고 아예 다른 스레드로 반환도 가능

## 3. CompletableFuture

## 4. Reactive Streams

## 5. Reactive Streams 구현 라이브러리

## 6. Java NIO

## 7. Reactor 패턴

### 스스로 질문해보기
```
동기와 비동기, Blocking과 Non-blocking의 차이는 무엇인가?
Reactive system의 필수 요소는 무엇인가?
Reactive system의 어떤 구조로 되어있나?
java nio는 어떻게 동작하나? java io와의 차이는 무엇인가?
reactor pattern을 사용해서 어떤 일을 할 수 있나?
reactive 란 무엇인가?
```