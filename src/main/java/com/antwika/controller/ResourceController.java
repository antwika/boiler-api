package com.antwika.controller;

import com.antwika.api.generated.api.ResourcesApi;
import com.antwika.api.generated.model.ResourceModel;
import com.antwika.service.ResourceService;
import com.antwika.util.LinkHeaderUtil;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

@Controller
public class ResourceController implements ResourcesApi {
  private final ResourceService resourceService;
  private final String baseUrl;
  private final String contextPath;

  @Autowired
  public ResourceController(
      ResourceService resourceService,
      @Value("${base-url}") String baseUrl,
      @Value("${server.servlet.context-path}") String contextPath) {
    this.resourceService = resourceService;
    this.baseUrl = baseUrl;
    this.contextPath = contextPath;
  }

  @Override
  public ResponseEntity<List<ResourceModel>> getResourcesOperation() {
    final var resources = resourceService.getResources();

    final var model =
        resources.stream()
            .map(
                resource ->
                    ResourceModel.builder().id(resource.getId()).name(resource.getName()).build())
            .toList();

    final var headers =
        HttpHeaders.readOnlyHttpHeaders(
            MultiValueMap.fromMultiValue(
                Map.of(
                    "Link",
                    List.of(
                        LinkHeaderUtil.format(
                            "self", String.format("%s%s/resources", baseUrl, contextPath))))));

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .headers(headers)
        .body(model);
  }

  @Override
  public ResponseEntity<ResourceModel> getResourceOperation(UUID id) {
    final var optionalResource = resourceService.getResource(id);

    if (optionalResource.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    final var resource = optionalResource.get();

    final var model = ResourceModel.builder().id(resource.getId()).name(resource.getName()).build();

    final var headers =
        HttpHeaders.readOnlyHttpHeaders(
            MultiValueMap.fromMultiValue(
                Map.of(
                    "Link",
                    List.of(
                        LinkHeaderUtil.format(
                            "self",
                            String.format(
                                "%s%s/resources/%s", baseUrl, contextPath, id.toString()))))));

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .headers(headers)
        .body(model);
  }
}
