package com.antwika.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.antwika.api.generated.entities.Resource;
import com.antwika.api.generated.model.ResourceModel;
import com.antwika.service.ResourceService;
import com.antwika.util.LinkHeaderUtil;
import com.antwika.util.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResourceControllerTest {
  @Test
  public void postResourceOperation() {
    // Arrange
    final var mockResourceService = mock(ResourceService.class);
    final var mockResourceModel = mock(ResourceModel.class);
    final var mockResource = mock(Resource.class);
    final var mockSavedResource = mock(Resource.class);
    final var mockSavedResourceModel = mock(ResourceModel.class);
    final var mockSavedResourceModelId = mock(UUID.class);
    final var mockResponseEntityBodyBuilder = mock(ResponseEntity.BodyBuilder.class);
    final var mockResponseEntity = mock(ResponseEntity.class);

    when(mockResourceService.createResource(mockResource)).thenReturn(mockSavedResource);
    when(mockSavedResourceModel.getId()).thenReturn(mockSavedResourceModelId);
    when(mockSavedResourceModelId.toString()).thenReturn("mock-saved-resource-model-id");

    when(mockResponseEntityBodyBuilder.headers(any(HttpHeaders.class)))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.contentType(MediaType.APPLICATION_JSON))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.body(mockSavedResourceModel)).thenReturn(mockResponseEntity);

    try (final var staticModelMapper = mockStatic(ModelMapper.class);
        final var staticLinkHeaderUtil = mockStatic(LinkHeaderUtil.class);
        final var constructHttpHeaders = mockConstruction(HttpHeaders.class);
        final var staticResponseEntity = mockStatic(ResponseEntity.class)) {

      staticModelMapper
          .when(() -> ModelMapper.modelToEntity(mockResourceModel))
          .thenReturn(mockResource);

      staticModelMapper
          .when(() -> ModelMapper.entityToModel(mockSavedResource))
          .thenReturn(mockSavedResourceModel);

      staticLinkHeaderUtil
          .when(() -> LinkHeaderUtil.getSelfLink(any()))
          .thenReturn("example-link-self-value");

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.CREATED))
          .thenReturn(mockResponseEntityBodyBuilder);

      // Act
      final var controller =
          new ResourceController(mockResourceService, "http://localhost:8080", "/v0");
      final var responseEntity = controller.postResourceOperation(mockResourceModel);

      // Assert
      assertEquals(1, constructHttpHeaders.constructed().size());

      final var mockHttpHeaders = constructHttpHeaders.constructed().get(0);

      verify(mockHttpHeaders, times(2)).add(any(), any());
      verify(mockHttpHeaders, times(1)).add(HttpHeaders.LINK, "example-link-self-value");
      verify(mockHttpHeaders, times(1))
          .add(
              HttpHeaders.LOCATION,
              "http://localhost:8080/v0/resources/mock-saved-resource-model-id");

      assertEquals(mockResponseEntity, responseEntity);
    }
  }

  @Test
  public void getResourcesOperation() {
    // Arrange
    final var mockResourceService = mock(ResourceService.class);
    final var mockResource = mock(Resource.class);
    final var mockResourceModel = mock(ResourceModel.class);
    final var mockResponseEntityBodyBuilder = mock(ResponseEntity.BodyBuilder.class);
    final var mockResponseEntity = mock(ResponseEntity.class);

    when(mockResourceService.getResources()).thenReturn(List.of(mockResource));

    when(mockResponseEntityBodyBuilder.headers(any(HttpHeaders.class)))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.contentType(MediaType.APPLICATION_JSON))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.body(List.of(mockResourceModel)))
        .thenReturn(mockResponseEntity);

    try (final var staticModelMapper = mockStatic(ModelMapper.class);
        final var staticLinkHeaderUtil = mockStatic(LinkHeaderUtil.class);
        final var constructHttpHeaders = mockConstruction(HttpHeaders.class);
        final var staticResponseEntity = mockStatic(ResponseEntity.class)) {

      staticModelMapper
          .when(() -> ModelMapper.entitiesToModels(List.of(mockResource)))
          .thenReturn(List.of(mockResourceModel));

      staticLinkHeaderUtil.when(LinkHeaderUtil::getSelfLink).thenReturn("example-link-self-value");

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.OK))
          .thenReturn(mockResponseEntityBodyBuilder);

      // Act
      final var controller =
          new ResourceController(mockResourceService, "http://localhost:8080", "/v0");
      final var responseEntity = controller.getResourcesOperation();

      // Assert
      assertEquals(1, constructHttpHeaders.constructed().size());

      final var mockHttpHeaders = constructHttpHeaders.constructed().get(0);

      verify(mockHttpHeaders, times(1)).add(any(), any());
      verify(mockHttpHeaders, times(1)).add(HttpHeaders.LINK, "example-link-self-value");

      assertEquals(mockResponseEntity, responseEntity);
    }
  }

  @Test
  public void getResourceOperation() {
    // Arrange
    final var mockResourceService = mock(ResourceService.class);
    final var mockResource = mock(Resource.class);
    final var mockResourceId = mock(UUID.class);
    final var mockResourceModel = mock(ResourceModel.class);
    final var mockResponseEntityBodyBuilder = mock(ResponseEntity.BodyBuilder.class);
    final var mockResponseEntity = mock(ResponseEntity.class);

    when(mockResourceId.toString()).thenReturn("mock-resource-id");
    when(mockResourceService.getResource(mockResourceId)).thenReturn(Optional.of(mockResource));

    when(mockResponseEntityBodyBuilder.headers(any(HttpHeaders.class)))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.contentType(MediaType.APPLICATION_JSON))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.body(mockResourceModel)).thenReturn(mockResponseEntity);

    try (final var staticModelMapper = mockStatic(ModelMapper.class);
        final var staticLinkHeaderUtil = mockStatic(LinkHeaderUtil.class);
        final var constructHttpHeaders = mockConstruction(HttpHeaders.class);
        final var staticResponseEntity = mockStatic(ResponseEntity.class)) {

      staticModelMapper
          .when(() -> ModelMapper.entityToModel(mockResource))
          .thenReturn(mockResourceModel);

      staticLinkHeaderUtil
          .when(() -> LinkHeaderUtil.getSelfLink(any()))
          .thenReturn("example-link-self-value");

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.OK))
          .thenReturn(mockResponseEntityBodyBuilder);

      // Act
      final var controller =
          new ResourceController(mockResourceService, "http://localhost:8080", "/v0");
      final var responseEntity = controller.getResourceOperation(mockResourceId);

      // Assert
      assertEquals(1, constructHttpHeaders.constructed().size());

      final var mockHttpHeaders = constructHttpHeaders.constructed().get(0);

      verify(mockHttpHeaders, times(1)).add(any(), any());
      verify(mockHttpHeaders, times(1)).add(HttpHeaders.LINK, "example-link-self-value");

      assertEquals(mockResponseEntity, responseEntity);
    }
  }

  @Test
  public void deleteResourceOperation() {
    // Arrange
    final var mockResourceService = mock(ResourceService.class);
    final var mockResource = mock(Resource.class);
    final var mockResourceId = mock(UUID.class);
    final var mockResourceModel = mock(ResourceModel.class);
    final var mockResponseEntityBodyBuilder = mock(ResponseEntity.BodyBuilder.class);
    final var mockResponseEntity = mock(ResponseEntity.class);

    when(mockResourceId.toString()).thenReturn("mock-resource-id");
    when(mockResourceService.deleteResource(mockResourceId)).thenReturn(mockResource);

    when(mockResponseEntityBodyBuilder.headers(any(HttpHeaders.class)))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.contentType(MediaType.APPLICATION_JSON))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.body(mockResourceModel)).thenReturn(mockResponseEntity);

    try (final var staticModelMapper = mockStatic(ModelMapper.class);
        final var staticLinkHeaderUtil = mockStatic(LinkHeaderUtil.class);
        final var constructHttpHeaders = mockConstruction(HttpHeaders.class);
        final var staticResponseEntity = mockStatic(ResponseEntity.class)) {

      staticModelMapper
          .when(() -> ModelMapper.entityToModel(mockResource))
          .thenReturn(mockResourceModel);

      staticLinkHeaderUtil
          .when(() -> LinkHeaderUtil.getSelfLink(any()))
          .thenReturn("example-link-self-value");

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.OK))
          .thenReturn(mockResponseEntityBodyBuilder);

      // Act
      final var controller =
          new ResourceController(mockResourceService, "http://localhost:8080", "/v0");
      final var responseEntity = controller.deleteResourceOperation(mockResourceId);

      // Assert
      assertEquals(1, constructHttpHeaders.constructed().size());

      final var mockHttpHeaders = constructHttpHeaders.constructed().get(0);

      verify(mockHttpHeaders, times(1)).add(any(), any());
      verify(mockHttpHeaders, times(1)).add(HttpHeaders.LINK, "example-link-self-value");

      assertEquals(mockResponseEntity, responseEntity);
    }
  }
}
