package com.antwika.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.antwika.api.generated.entities.Resource;
import com.antwika.repository.ResourceRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class ResourceServiceTest {
  @Test
  public void getResources() {
    // Arrange
    final var mockResourceRepository = mock(ResourceRepository.class);
    final var mockResourceEntity = mock(Resource.class);

    when(mockResourceRepository.findAll()).thenReturn(List.of(mockResourceEntity));

    // Act
    final var resourceService = new ResourceService(mockResourceRepository);
    final var resources = resourceService.getResources();

    // Assert
    assertEquals(List.of(mockResourceEntity), resources);
  }

  @Test
  public void getResource() {
    // Arrange
    final var mockResourceId = mock(UUID.class);
    final var mockResourceRepository = mock(ResourceRepository.class);
    final var mockResourceEntity = mock(Resource.class);

    when(mockResourceRepository.findById(mockResourceId))
        .thenReturn(Optional.of(mockResourceEntity));

    // Act
    final var resourceService = new ResourceService(mockResourceRepository);
    final var resourceEntity = resourceService.getResource(mockResourceId);

    // Assert
    assertEquals(Optional.of(mockResourceEntity), resourceEntity);
  }

  @Test
  public void createResource() {
    // Arrange
    final var mockGeneratedResourceId = mock(UUID.class);
    final var mockResource = mock(Resource.class);
    final var mockResourceRepository = mock(ResourceRepository.class);
    final var mockSavedResource = mock(Resource.class);

    when(mockResourceRepository.save(mockResource)).thenReturn(mockSavedResource);

    try (final var staticUUID = mockStatic(UUID.class)) {
      staticUUID.when(() -> UUID.randomUUID()).thenReturn(mockGeneratedResourceId);

      // Act
      final var resourceService = new ResourceService(mockResourceRepository);
      final var resourceEntity = resourceService.createResource(mockResource);

      // Assert
      verify(mockResource, times(1)).setId(mockGeneratedResourceId);
      assertEquals(mockSavedResource, resourceEntity);
    }
  }

  @Test
  public void deleteResource() {
    // Arrange
    final var mockResourceId = mock(UUID.class);
    final var mockResourceRepository = mock(ResourceRepository.class);
    final var mockResourceEntity = mock(Resource.class);
    final var mockNow = mock(ZonedDateTime.class);
    final var mockDeletedResourceEntity = mock(Resource.class);

    when(mockResourceRepository.findById(mockResourceId))
        .thenReturn(Optional.of(mockResourceEntity));

    when(mockResourceRepository.save(mockResourceEntity)).thenReturn(mockDeletedResourceEntity);

    try (final var staticZonedDateTime = mockStatic(ZonedDateTime.class)) {

      staticZonedDateTime.when(ZonedDateTime::now).thenReturn(mockNow);

      // Act
      final var resourceService = new ResourceService(mockResourceRepository);
      final var resourceEntity = resourceService.deleteResource(mockResourceId);

      // Assert
      verify(mockResourceEntity, times(1)).setDeleted(mockNow);
      assertEquals(mockDeletedResourceEntity, resourceEntity);
    }
  }
}
