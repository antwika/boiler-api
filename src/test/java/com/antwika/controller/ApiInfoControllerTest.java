package com.antwika.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.antwika.api.generated.model.ApiInfoModel;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ApiInfoControllerTest {
  @Test
  public void apiInfo() {
    // Arrange
    final var mockResponseEntityBodyBuilder = mock(ResponseEntity.BodyBuilder.class);
    final var mockResponseEntity = mock(ResponseEntity.class);

    final var mockApiInfoApiInfoBuilder = mock(ApiInfoModel.ApiInfoModelBuilder.class);
    final var mockApiInfo = mock(ApiInfoModel.class);

    when(mockResponseEntityBodyBuilder.contentType(MediaType.APPLICATION_JSON))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.body(any(ApiInfoModel.class)))
        .thenReturn(mockResponseEntity);

    when(mockApiInfoApiInfoBuilder.apiName(anyString())).thenReturn(mockApiInfoApiInfoBuilder);
    when(mockApiInfoApiInfoBuilder.apiVersion(anyString())).thenReturn(mockApiInfoApiInfoBuilder);
    when(mockApiInfoApiInfoBuilder.build()).thenReturn(mockApiInfo);

    try (final var staticResponseEntity = mockStatic(ResponseEntity.class);
        final var staticApiInfo = mockStatic(ApiInfoModel.class)) {

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.OK))
          .thenReturn(mockResponseEntityBodyBuilder);
      staticApiInfo.when(ApiInfoModel::builder).thenReturn(mockApiInfoApiInfoBuilder);

      // Act
      final var apiInfoController = new ApiInfoController("appProjectName", "appProjectVersion");
      final var entity = apiInfoController.getApiInfoOperation();

      // Assert
      verify(mockApiInfoApiInfoBuilder, times(1)).name("appProjectName");
      verify(mockApiInfoApiInfoBuilder, times(1)).version("appProjectVersion");
      verify(mockResponseEntityBodyBuilder, times(1)).body(mockApiInfo);
      assertEquals(mockResponseEntity, entity);
    }
  }
}
