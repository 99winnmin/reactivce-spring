package com.example.reactorPractice.reactor.repository;

import com.example.reactorPractice.common.repository.ArticleEntity;
import com.example.reactorPractice.common.repository.UserEntity;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class UserReactorRepository {
  private final Map<String, UserEntity> userMap;

  public UserReactorRepository() {
    var user = new UserEntity("1234", "taewoo", 32, "image#1000");

    userMap = Map.of("1234", user);
  }

  @SneakyThrows
  public Mono<UserEntity> findById(String userId) {
    return Mono.create(monoSink -> {
      log.info("UserRepository.findById: {}", userId);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      UserEntity userEntity = userMap.get(userId);
      if (userEntity == null) {
        monoSink.success();
      } else {
        monoSink.success(userEntity);
      }
    });
  }
}
