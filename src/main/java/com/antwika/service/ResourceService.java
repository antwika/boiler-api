package com.antwika.service;

import com.antwika.api.generated.entities.Resource;
import com.antwika.exception.ResourceNotFoundException;
import com.antwika.repository.ResourceRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
  private final ResourceRepository resourceRepository;

  @Autowired
  public ResourceService(ResourceRepository resourceRepository) {
    this.resourceRepository = resourceRepository;
  }

  public List<Resource> getResources() {
    return resourceRepository.findAll();
  }

  public Page<Resource> getResources(Pageable pageable) {
    return resourceRepository.findAll(pageable);
  }

  public Optional<Resource> getResource(UUID id) {
    return resourceRepository.findById(id);
  }

  public Resource createResource(Resource resource) {
    resource.setId(UUID.randomUUID());
    resource.setCreated(ZonedDateTime.now());
    resource.setUpdated(ZonedDateTime.now());
    return resourceRepository.save(resource);
  }

  public Resource deleteResource(UUID id) {
    final var resource =
        resourceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    resource.setDeleted(ZonedDateTime.now());
    return resourceRepository.save(resource);
  }
}
