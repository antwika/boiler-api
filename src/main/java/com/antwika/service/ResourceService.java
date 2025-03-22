package com.antwika.service;

import com.antwika.api.generated.entities.Resource;
import com.antwika.repository.ResourceRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
  private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

  private final ResourceRepository resourceRepository;

  @Autowired
  public ResourceService(ResourceRepository resourceRepository) {
    this.resourceRepository = resourceRepository;
  }

  public List<Resource> getResources() {
    return resourceRepository.findAll();
  }

  public Optional<Resource> getResource(UUID id) {
    logger.debug("Getting resource by id: {}", id);
    return resourceRepository.findById(id);
  }
}
