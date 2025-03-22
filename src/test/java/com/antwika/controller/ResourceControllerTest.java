package com.antwika.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.antwika.api.generated.entities.Resource;
import com.antwika.api.generated.model.ResourceModel;
import com.antwika.service.ResourceService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResourceControllerTest {
  @Test
  public void getResourcesOperation() {
    // Arrange
    final var mockResourceService = mock(ResourceService.class);
    final var mockResource = mock(Resource.class);
    final var mockResourceId = mock(UUID.class);
    final var mockResourceName = "mock-resource-name";
    final var mockResourceBuilder = mock(ResourceModel.ResourceModelBuilder.class);
    final var mockResourceModel = mock(ResourceModel.class);
    final var mockResponseEntityBodyBuilder = mock(ResponseEntity.BodyBuilder.class);
    final var mockResponseEntity = mock(ResponseEntity.class);

    when(mockResourceService.getResources()).thenReturn(List.of(mockResource));
    when(mockResource.getId()).thenReturn(mockResourceId);
    when(mockResource.getName()).thenReturn(mockResourceName);
    when(mockResourceBuilder.id(mockResourceId)).thenReturn(mockResourceBuilder);
    when(mockResourceBuilder.name(mockResourceName)).thenReturn(mockResourceBuilder);
    when(mockResourceBuilder.build()).thenReturn(mockResourceModel);
    when(mockResponseEntityBodyBuilder.contentType(MediaType.APPLICATION_JSON))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.body(List.of(mockResourceModel)))
        .thenReturn(mockResponseEntity);

    try (final var staticResourceModel = mockStatic(ResourceModel.class);
        final var staticResponseEntity = mockStatic(ResponseEntity.class)) {

      staticResourceModel.when(ResourceModel::builder).thenReturn(mockResourceBuilder);

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.OK))
          .thenReturn(mockResponseEntityBodyBuilder);

      // Act
      final var controller = new ResourceController(mockResourceService);
      final var response = controller.getResourcesOperation();

      // Assert
      assertEquals(mockResponseEntity, response);
    }
  }

  @Test
  public void getResourceOperation() {
    // Arrange
    final var mockResourceService = mock(ResourceService.class);
    final var mockResourceId = mock(UUID.class);
    final var mockResourceName = "example-resource-name";
    final var mockResource = mock(Resource.class);
    final var mockResourceBuilder = mock(ResourceModel.ResourceModelBuilder.class);
    final var mockResourceModel = mock(ResourceModel.class);
    final var mockResponseEntityBodyBuilder = mock(ResponseEntity.BodyBuilder.class);
    final var mockResponseEntity = mock(ResponseEntity.class);

    when(mockResourceService.getResource(mockResourceId)).thenReturn(Optional.of(mockResource));
    when(mockResource.getId()).thenReturn(mockResourceId);
    when(mockResource.getName()).thenReturn(mockResourceName);
    when(mockResourceBuilder.id(mockResourceId)).thenReturn(mockResourceBuilder);
    when(mockResourceBuilder.name(mockResourceName)).thenReturn(mockResourceBuilder);
    when(mockResourceBuilder.build()).thenReturn(mockResourceModel);
    when(mockResponseEntityBodyBuilder.contentType(MediaType.APPLICATION_JSON))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.body(mockResourceModel)).thenReturn(mockResponseEntity);

    try (final var staticResource = mockStatic(ResourceModel.class);
        final var staticResponseEntity = mockStatic(ResponseEntity.class)) {

      staticResource.when(ResourceModel::builder).thenReturn(mockResourceBuilder);

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.OK))
          .thenReturn(mockResponseEntityBodyBuilder);

      // Act
      final var controller = new ResourceController(mockResourceService);
      final var responseEntity = controller.getResourceOperation(mockResourceId);

      // Assert
      assertEquals(mockResponseEntity, responseEntity);
    }
  }

  @Test
  public void getResource_whenResourceNotFound_returnHttpNotFoundResponseEntity() {
    // Arrange
    final var mockResourceService = mock(ResourceService.class);
    final var mockResourceId = mock(UUID.class);
    final var mockResponseEntityBodyBuilder = mock(ResponseEntity.BodyBuilder.class);
    final var mockResponseEntity = mock(ResponseEntity.class);

    when(mockResourceService.getResource(mockResourceId)).thenReturn(Optional.empty());
    when(mockResponseEntityBodyBuilder.build()).thenReturn(mockResponseEntity);

    try (final var staticResponseEntity = mockStatic(ResponseEntity.class)) {

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.NOT_FOUND))
          .thenReturn(mockResponseEntityBodyBuilder);

      // Act
      final var controller = new ResourceController(mockResourceService);
      final var responseEntity = controller.getResourceOperation(mockResourceId);

      // Assert
      assertEquals(mockResponseEntity, responseEntity);
    }
  }
}
