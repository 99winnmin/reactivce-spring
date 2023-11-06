package com.example.basic;

import java.util.concurrent.Flow;
import lombok.SneakyThrows;

public class PubSubExample {

  @SneakyThrows
  public static void main(String[] args) {
    Flow.Publisher publisher = new FixedIntPublisher();
//    Flow.Subscriber subscriber = new RequestNSubscriber<>(1);
    Flow.Subscriber subscriber = new RequestNSubscriber<>(3);
//    Flow.Subscriber subscriber = new RequestNSubscriber<>(Integer.MAX_VALUE);
    publisher.subscribe(subscriber);

    Thread.sleep(100);

    /*
     Hot Publisher - 실시간성이 중요한 데이터
     - subscriber 가 없더라도 데이터를 생성하고 stream 에 push 하는 publisher
     - 트위터 게시글 읽기, 공유 리소스 변화 등
     - 여러 subscriber 에게 동일한 데이터 전달
     */

    /*
     Cold Publisher - subscriber 에게만 중요한 데이터를 전송할
     - subscribe 가 시작되는 순간부터 데이터를 생성하고 전송
     - 파일 읽기, 웹 API 요청 등
     - subscriber 에 따라 독립적인 데이터 스트림 제공
     */
  }
}
