package com.example.reactorPractice.blocking.repository;

import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FollowRepository {
  private Map<String, Long> userFollowCountMap;

  public FollowRepository() {
    userFollowCountMap = Map.of("1234", 1000L);
  }

  @SneakyThrows
  public Long countByUserId(String userId) {
    log.info("FollowRepository.countByUserId: {}", userId);
    Thread.sleep(1000);
    return userFollowCountMap.getOrDefault(userId, 0L);
  }
}
