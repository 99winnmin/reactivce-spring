package com.example.reactorPractice.reactor;

import com.example.reactorPractice.common.Article;
import com.example.reactorPractice.common.EmptyImage;
import com.example.reactorPractice.common.Image;
import com.example.reactorPractice.common.User;
import com.example.reactorPractice.common.repository.UserEntity;
import com.example.reactorPractice.reactor.repository.ArticleReactorRepository;
import com.example.reactorPractice.reactor.repository.FollowReactorRepository;
import com.example.reactorPractice.reactor.repository.ImageReactorRepository;
import com.example.reactorPractice.reactor.repository.UserReactorRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
@RequiredArgsConstructor
public class UserReactorService {
  private final UserReactorRepository userReactorRepository;
  private final ArticleReactorRepository articleReactorRepository;
  private final ImageReactorRepository imageReactorRepository;
  private final FollowReactorRepository followReactorRepository;

  @SneakyThrows
  public Mono<User> getUserById(String id) {
      return userReactorRepository.findById(id)
          .flatMap(this::getUser);

  }

  @SneakyThrows
  private Mono<User> getUser(UserEntity userEntity) {
      Context context = Context.of("user", userEntity);

    var imageMono = imageReactorRepository.findWithContext()
        .map(imageEntity ->
                new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl())
        ).onErrorReturn(new EmptyImage())
        .contextWrite(context);

    var articlesMono = articleReactorRepository.findAllWithContext()
        .skip(5)
        .take(2)
        .map(articleEntity ->
            new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent())
        ).collectList()
        .contextWrite(context);

    var followCountMono = followReactorRepository.countWithContext()
        .contextWrite(context);

//    return Flux.mergeSequential(imageMono, articlesMono, followCountMono)
//        .collectList()
//        .map(resultList -> {
//            Image image = (Image) resultList.get(0);
//            List<Article> articles = (List<Article>) resultList.get(1);
//            Long followCount = (Long) resultList.get(2);
//
//            Optional<Image> imageOptional = Optional.empty();
//            if (!(image instanceof EmptyImage)) {
//                imageOptional = Optional.of(image);
//            }
//
//            return new User(
//                userEntity.getId(),
//                userEntity.getName(),
//                userEntity.getAge(),
//                imageOptional,
//                articles,
//                followCount);
//        });
      return Mono.zip(imageMono, articlesMono, followCountMono)
          .map(resultList -> {
              Image image = resultList.getT1();
              List<Article> articles = resultList.getT2();
              Long followCount = resultList.getT3();

              Optional<Image> imageOptional = Optional.empty();
              if (!(image instanceof EmptyImage)) {
                  imageOptional = Optional.of(image);
              }

              return new User(
                  userEntity.getId(),
                  userEntity.getName(),
                  userEntity.getAge(),
                  imageOptional,
                  articles,
                  followCount);
          });
  }
}
