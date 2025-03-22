package com.antwika.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.antwika.api.generated.model.ApiInfoModel;
import com.antwika.util.LinkHeaderUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
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
    when(mockResponseEntityBodyBuilder.headers(any(HttpHeaders.class)))
        .thenReturn(mockResponseEntityBodyBuilder);
    when(mockResponseEntityBodyBuilder.body(any(ApiInfoModel.class)))
        .thenReturn(mockResponseEntity);

    when(mockApiInfoApiInfoBuilder.apiName(anyString())).thenReturn(mockApiInfoApiInfoBuilder);
    when(mockApiInfoApiInfoBuilder.apiVersion(anyString())).thenReturn(mockApiInfoApiInfoBuilder);
    when(mockApiInfoApiInfoBuilder.apiDocumentation(anyString()))
        .thenReturn(mockApiInfoApiInfoBuilder);
    when(mockApiInfoApiInfoBuilder.apiStatus(anyString())).thenReturn(mockApiInfoApiInfoBuilder);
    when(mockApiInfoApiInfoBuilder.apiReleased(anyString())).thenReturn(mockApiInfoApiInfoBuilder);
    when(mockApiInfoApiInfoBuilder.build()).thenReturn(mockApiInfo);

    try (final var staticResponseEntity = mockStatic(ResponseEntity.class);
        final var staticApiInfo = mockStatic(ApiInfoModel.class);
        final var staticLinkHeaderUtil = mockStatic(LinkHeaderUtil.class)) {

      staticResponseEntity
          .when(() -> ResponseEntity.status(HttpStatus.OK))
          .thenReturn(mockResponseEntityBodyBuilder);

      staticApiInfo.when(ApiInfoModel::builder).thenReturn(mockApiInfoApiInfoBuilder);

      staticLinkHeaderUtil
          .when(() -> LinkHeaderUtil.format("self", "http://localhost:8080/v0/api-info"))
          .thenReturn("example-link-self-value");

      // Act
      final var apiInfoController =
          new ApiInfoController(
              "appProjectName",
              "appProjectVersion",
              "apiProjectUrl",
              "apiProjectStatus",
              "apiProjectReleased",
              "http://localhost:8080",
              "/v0");
      final var entity = apiInfoController.getApiInfoOperation();

      // Assert
      verify(mockApiInfoApiInfoBuilder, times(1)).apiName("appProjectName");
      verify(mockApiInfoApiInfoBuilder, times(1)).apiVersion("appProjectVersion");
      verify(mockApiInfoApiInfoBuilder, times(1)).apiDocumentation("apiProjectUrl");
      verify(mockApiInfoApiInfoBuilder, times(1)).apiStatus("apiProjectStatus");
      verify(mockApiInfoApiInfoBuilder, times(1)).apiReleased("apiProjectReleased");
      verify(mockResponseEntityBodyBuilder, times(1)).body(mockApiInfo);

      assertEquals(mockResponseEntity, entity);
    }
  }
}
