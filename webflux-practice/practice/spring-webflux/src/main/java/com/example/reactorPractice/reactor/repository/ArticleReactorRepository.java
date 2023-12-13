package com.example.reactorPractice.reactor.repository;

import com.example.reactorPractice.common.repository.ArticleEntity;
import com.example.reactorPractice.common.repository.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ArticleReactorRepository {
  private static List<ArticleEntity> articleEntities;

  public ArticleReactorRepository() {
    articleEntities = List.of(
        new ArticleEntity("1", "소식1", "내용1", "1234"),
        new ArticleEntity("2", "소식2", "내용2", "1234"),
        new ArticleEntity("3", "소식3", "내용3", "10000"),
        new ArticleEntity("4", "소식4", "내용4", "1234"),
        new ArticleEntity("5", "소식5", "내용5", "1234"),
        new ArticleEntity("6", "소식6", "내용6", "1234"),
        new ArticleEntity("7", "소식7", "내용7", "1234"),
        new ArticleEntity("8", "소식8", "내용8", "1234"),
        new ArticleEntity("9", "소식9", "내용9", "1234"),
        new ArticleEntity("10", "소식10", "내용10", "1234"),
        new ArticleEntity("11", "소식11", "내용11", "1234")
    );
  }

  @SneakyThrows
  public Flux<ArticleEntity> findAllByUserId(String userId) {
    return Flux.create(sink -> {
      log.info("ArticleRepository.findAllByUserId: {}", userId);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      // 이와 같이 리턴해도됌
//      var filtered = articleEntities.stream()
//          .filter(articleEntity -> articleEntity.getUserId().equals(userId));
//      return Flux.fromStream(filtered);

      articleEntities.stream()
          .filter(articleEntity -> articleEntity.getUserId().equals(userId))
          .forEach(sink::next); // 값 넘기기
      sink.complete();
    });
  }

  public Flux<ArticleEntity> findAllWithContext() {
    return Flux.deferContextual(context -> {
      Optional<UserEntity> userEntityOptional = context.getOrEmpty("user");

      if (userEntityOptional.isEmpty()) {
        throw new RuntimeException("user not found");
      }

      return Mono.just(userEntityOptional.get().getId());
    }).flatMap(this::findAllByUserId);
  }
}
