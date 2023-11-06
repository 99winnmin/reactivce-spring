package com.example.future;

import com.example.common.Article;
import com.example.common.Image;
import com.example.common.User;
import com.example.common.repository.UserEntity;
import com.example.future.repository.ArticleFutureRepository;
import com.example.future.repository.FollowFutureRepository;
import com.example.future.repository.ImageFutureRepository;
import com.example.future.repository.UserFutureRepository;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserFutureService {
  private final UserFutureRepository userFutureRepository;
  private final ArticleFutureRepository articleFutureRepository;
  private final ImageFutureRepository imageFutureRepository;
  private final FollowFutureRepository followFutureRepository;

  @SneakyThrows
  public CompletableFuture<Optional<User>> getUserById(String id) {
    return userFutureRepository.findById(id)
            .thenComposeAsync(this::getUser);

  }

  @SneakyThrows
  private CompletableFuture<Optional<User>> getUser(Optional<UserEntity> userEntityOptional) {
    if (userEntityOptional.isEmpty()) {
//      var future = new CompletableFuture<Optional<User>>();
//      future.complete(Optional.empty());
//      return future;
      return CompletableFuture.completedFuture(Optional.empty()); // 위 3줄과 같은 의미임
    }
    var user = userEntityOptional.get();

    // image, articles, followCount 를 각각의 스레드에서 가져오게
    // [ForkJoinPool.commonPool-worker-1] [ForkJoinPool.commonPool-worker-3] [ForkJoinPool.commonPool-worker-2]
    var imageFuture = imageFutureRepository.findById(
            user.getProfileImageId()) // .get() 을 하면 동기식으로 처리됌
        .thenApplyAsync(imageEntityOptional -> imageEntityOptional.map(imageEntity ->
                new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl())
            )
        );

    var articlesFuture = articleFutureRepository.findAllByUserId(user.getId())
        .thenApplyAsync(articleEntities -> articleEntities.stream()
            .map(articleEntity ->
                new Article(articleEntity.getId(), articleEntity.getTitle(),
                    articleEntity.getContent())
            )
            .collect(Collectors.toList())
        );

    var followCountFuture = followFutureRepository.countByUserId(user.getId());

    // 여기서 get() 들을 실행하게 되면 각각 기다리게됌
//    var image = imageFuture.get();
//    var articles = articlesFuture.get();
//    var followCount = followCountFuture.get();

    return CompletableFuture.allOf(imageFuture, articlesFuture, followCountFuture)
        .thenAcceptAsync(v -> {
          log.info("Three Futures are all done");
        })
        .thenRunAsync(() -> {
          log.info("All Futures are done");
        })
        .thenApplyAsync(v -> {
          try {
            // 여기서 get() 즉시 리턴됌, isDone() 이 true 상태이기 때문에
            var image = imageFuture.get();
            var articles = articlesFuture.get();
            var followCount = followCountFuture.get();

            return Optional.of(
                new User(
                    user.getId(),
                    user.getName(),
                    user.getAge(),
                    image,
                    articles,
                    followCount
                )
            );
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
  }
}
