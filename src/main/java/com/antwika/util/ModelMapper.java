package com.antwika.util;

import com.antwika.api.generated.entities.Resource;
import com.antwika.api.generated.model.ResourceModel;

public class ModelMapper {
  private ModelMapper() {}

  public static ResourceModel entityToModel(Resource entity) {
    return ResourceModel.builder()
        .id(entity.getId())
        .name(entity.getName())
        .created(entity.getCreated())
        .updated(entity.getUpdated())
        .deleted(entity.getDeleted())
        .build();
  }

  public static Resource modelToEntity(ResourceModel model) {
    final var entity =
        new Resource(model.getId(), model.getName(), model.getCreated(), model.getUpdated());
    entity.setDeleted(model.getDeleted());
    return entity;
  }
}
