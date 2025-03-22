package com.antwika.controller;

import com.antwika.api.generated.api.ApiInfoApi;
import com.antwika.api.generated.model.ApiInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ApiInfoController implements ApiInfoApi {
  private final String appProjectName;
  private final String appProjectVersion;

  @Autowired
  public ApiInfoController(
      @Value("${app.project.name}") String appProjectName,
      @Value("${app.project.version}") String appProjectVersion) {
    this.appProjectName = appProjectName;
    this.appProjectVersion = appProjectVersion;
  }

  @Override
  public ResponseEntity<ApiInfoModel> getApiInfoOperation() {
    final var model =
        ApiInfoModel.builder().apiName(appProjectName).apiVersion(appProjectVersion).build();
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(model);
  }
}
