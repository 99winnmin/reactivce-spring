package com.example.reactorPractice.blocking.repository;

import com.example.reactorPractice.common.repository.ImageEntity;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageRepository {
  private final Map<String, ImageEntity> imageMap;

  public ImageRepository() {
    imageMap = Map.of(
        "image#1000", new ImageEntity("image#1000", "profileImage", "https://dailyone.com/images/1000")
    );
  }

  @SneakyThrows
  public Optional<ImageEntity> findById(String id) {
    log.info("ImageRepository.findById: {}", id);
    Thread.sleep(1000);
    return Optional.ofNullable(imageMap.get(id));
  }
}
