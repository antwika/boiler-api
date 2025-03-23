package com.antwika.controller;

import com.antwika.api.generated.api.ResourcesApi;
import com.antwika.api.generated.model.ResourceModel;
import com.antwika.exception.ResourceNotFoundException;
import com.antwika.service.ResourceService;
import com.antwika.util.LinkHeaderUtil;
import com.antwika.util.ModelMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

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
  public ResponseEntity<ResourceModel> postResourceOperation(ResourceModel resourceModel) {

    final var resource = ModelMapper.modelToEntity(resourceModel);
    final var savedResource = resourceService.createResource(resource);
    final var savedResourceModel = ModelMapper.entityToModel(savedResource);

    final var headers = new HttpHeaders();

    headers.add(
        HttpHeaders.LINK,
        LinkHeaderUtil.format("self", String.format("%s%s/resources", baseUrl, contextPath)));

    headers.add(
        HttpHeaders.LOCATION,
        String.format(
            "%s%s/resources/%s", baseUrl, contextPath, savedResourceModel.getId().toString()));

    return ResponseEntity.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .headers(headers)
        .body(savedResourceModel);
  }

  @Override
  public ResponseEntity<List<ResourceModel>> getResourcesOperation() {
    final var resources = resourceService.getResources();

    final var model = resources.stream().map(ModelMapper::entityToModel).toList();

    final var headers = new HttpHeaders();

    headers.add(
        HttpHeaders.LINK,
        LinkHeaderUtil.format("self", String.format("%s%s/resources", baseUrl, contextPath)));

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .headers(headers)
        .body(model);
  }

  @Override
  public ResponseEntity<ResourceModel> getResourceOperation(UUID id) {
    final var resource =
        resourceService.getResource(id).orElseThrow(ResourceNotFoundException::new);

    final var model = ModelMapper.entityToModel(resource);

    final var headers = new HttpHeaders();

    headers.add(
        HttpHeaders.LINK,
        LinkHeaderUtil.format(
            "self", String.format("%s%s/resources/%s", baseUrl, contextPath, id.toString())));

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .headers(headers)
        .body(model);
  }

  @Override
  public ResponseEntity<ResourceModel> deleteResourceOperation(UUID id) {
    final var deletedResource = resourceService.deleteResource(id);

    final var model = ModelMapper.entityToModel(deletedResource);

    final var headers = new HttpHeaders();

    headers.add(
        HttpHeaders.LINK,
        LinkHeaderUtil.format(
            "self", String.format("%s%s/resources/%s", baseUrl, contextPath, id.toString())));

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .headers(headers)
        .body(model);
  }
}
