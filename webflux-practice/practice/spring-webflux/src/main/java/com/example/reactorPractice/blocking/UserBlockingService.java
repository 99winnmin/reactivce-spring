package com.example.reactorPractice.blocking;

import com.example.reactorPractice.blocking.repository.ArticleRepository;
import com.example.reactorPractice.blocking.repository.FollowRepository;
import com.example.reactorPractice.blocking.repository.ImageRepository;
import com.example.reactorPractice.blocking.repository.UserRepository;
import com.example.reactorPractice.common.Article;
import com.example.reactorPractice.common.Image;
import com.example.reactorPractice.common.User;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserBlockingService {
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final ImageRepository imageRepository;
  private final FollowRepository followRepository;

  public Optional<User> getUserById(String id) {
    return userRepository.findById(id)
        .map(user -> {
          var image = imageRepository.findById(user.getProfileImageId())
              .map(imageEntity -> {
                return new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl());
              });

          var articles = articleRepository.findAllByUserId(user.getId())
              .stream().map(articleEntity ->
                  new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent()))
              .collect(Collectors.toList());

          var followCount = followRepository.countByUserId(user.getId());

          return new User(
              user.getId(),
              user.getName(),
              user.getAge(),
              image,
              articles,
              followCount
          );
        });
  }
}
