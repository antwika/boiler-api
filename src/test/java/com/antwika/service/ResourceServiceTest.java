package com.antwika.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.antwika.api.generated.entities.Resource;
import com.antwika.repository.ResourceRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceServiceTest {
  @Test
  public void getResources() {
    // Arrange
    final var mockLogger = mock(Logger.class);
    final var mockResourceRepository = mock(ResourceRepository.class);
    final var mockResourceEntity = mock(Resource.class);

    when(mockResourceRepository.findAll()).thenReturn(List.of(mockResourceEntity));

    try (final var staticLoggerFactory = mockStatic(LoggerFactory.class)) {

      staticLoggerFactory
          .when(() -> LoggerFactory.getLogger(ResourceService.class))
          .thenReturn(mockLogger);

      // Act
      final var resourceService = new ResourceService(mockResourceRepository);
      final var resources = resourceService.getResources();

      // Assert
      assertEquals(List.of(mockResourceEntity), resources);
    }
  }

  @Test
  public void getResource() {
    // Arrange
    final var mockLogger = mock(Logger.class);
    final var mockResourceId = mock(UUID.class);
    final var mockResourceRepository = mock(ResourceRepository.class);
    final var mockResourceEntity = mock(Resource.class);

    when(mockResourceRepository.findById(mockResourceId))
        .thenReturn(Optional.of(mockResourceEntity));

    try (final var staticLoggerFactory = mockStatic(LoggerFactory.class)) {

      staticLoggerFactory
          .when(() -> LoggerFactory.getLogger(ResourceService.class))
          .thenReturn(mockLogger);

      // Act
      final var resourceService = new ResourceService(mockResourceRepository);
      final var resourceEntity = resourceService.getResource(mockResourceId);

      // Assert
      // verify(mockLogger, times(1)).debug("Getting resource by id: {}", 123L);
      assertEquals(Optional.of(mockResourceEntity), resourceEntity);
    }
  }
}
