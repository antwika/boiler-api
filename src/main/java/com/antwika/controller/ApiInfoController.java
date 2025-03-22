package com.antwika.controller;

import com.antwika.api.generated.api.ApiInfoApi;
import com.antwika.api.generated.model.ApiInfoModel;
import com.antwika.util.LinkHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Controller
public class ApiInfoController implements ApiInfoApi {
  private final String appProjectName;
  private final String appProjectVersion;
  private final String appProjectUrl;
  private final String appProjectStatus;
  private final String appProjectReleased;
  private final String baseUrl;
  private final String contextPath;

  @Autowired
  public ApiInfoController(
      @Value("${app.project.name}") String appProjectName,
      @Value("${app.project.version}") String appProjectVersion,
      @Value("${app.project.url}") String appProjectUrl,
      @Value("${app.project.status}") String appProjectStatus,
      @Value("${app.project.released}") String appProjectReleased,
      @Value("${base-url}") String baseUrl,
      @Value("${server.servlet.context-path}") String contextPath) {
    this.appProjectName = appProjectName;
    this.appProjectVersion = appProjectVersion;
    this.appProjectUrl = appProjectUrl;
    this.appProjectStatus = appProjectStatus;
    this.appProjectReleased = appProjectReleased;
    this.baseUrl = baseUrl;
    this.contextPath = contextPath;
  }

  @Override
  public ResponseEntity<ApiInfoModel> getApiInfoOperation() {
    final var model =
        ApiInfoModel.builder()
            .apiName(appProjectName)
            .apiVersion(appProjectVersion)
            .apiDocumentation(appProjectUrl)
            .apiReleased(appProjectReleased)
            .apiStatus(appProjectStatus)
            .build();

    final var headers =
        HttpHeaders.readOnlyHttpHeaders(
            MultiValueMap.fromMultiValue(
                Map.of(
                    "Link",
                    List.of(
                        LinkHeaderUtil.format(
                            "self", String.format("%s%s/api-info", baseUrl, contextPath))))));

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .headers(headers)
        .body(model);
  }
}
