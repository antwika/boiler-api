package com.antwika.controller;

import com.antwika.api.generated.api.ResourcesApi;
import com.antwika.api.generated.model.ResourceModel;
import com.antwika.service.ResourceService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ResourceController implements ResourcesApi {
  private final ResourceService resourceService;

  @Autowired
  public ResourceController(ResourceService resourceService) {
    this.resourceService = resourceService;
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

    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(model);
  }

  @Override
  public ResponseEntity<ResourceModel> getResourceOperation(UUID id) {
    final var optionalResource = resourceService.getResource(id);

    if (optionalResource.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    final var resource = optionalResource.get();

    final var model = ResourceModel.builder().id(resource.getId()).name(resource.getName()).build();
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(model);
  }
}
