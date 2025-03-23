package com.antwika.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.antwika.api.generated.entities.Resource;
import com.antwika.api.generated.model.ResourceModel;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class ModelMapperTest {
  @Test
  public void entityToModel() {
    // Arrange
    final var mockResource = mock(Resource.class);
    final var mockResourceId = mock(UUID.class);
    final var mockResourceName = "mock-resource-name";
    final var mockResourceCreated = mock(ZonedDateTime.class);
    final var mockResourceUpdated = mock(ZonedDateTime.class);
    final var mockResourceDeleted = mock(ZonedDateTime.class);
    final var mockResourceModelBuilder = mock(ResourceModel.ResourceModelBuilder.class);
    final var mockResourceModel = mock(ResourceModel.class);

    when(mockResource.getId()).thenReturn(mockResourceId);
    when(mockResource.getName()).thenReturn(mockResourceName);
    when(mockResource.getCreated()).thenReturn(mockResourceCreated);
    when(mockResource.getUpdated()).thenReturn(mockResourceUpdated);
    when(mockResource.getDeleted()).thenReturn(mockResourceDeleted);

    when(mockResourceModelBuilder.id(mockResourceId)).thenReturn(mockResourceModelBuilder);
    when(mockResourceModelBuilder.name(mockResourceName)).thenReturn(mockResourceModelBuilder);
    when(mockResourceModelBuilder.created(mockResourceCreated))
        .thenReturn(mockResourceModelBuilder);
    when(mockResourceModelBuilder.updated(mockResourceUpdated))
        .thenReturn(mockResourceModelBuilder);
    when(mockResourceModelBuilder.deleted(mockResourceDeleted))
        .thenReturn(mockResourceModelBuilder);
    when(mockResourceModelBuilder.build()).thenReturn(mockResourceModel);

    try (final var staticResourceModel = mockStatic(ResourceModel.class)) {

      staticResourceModel.when(ResourceModel::builder).thenReturn(mockResourceModelBuilder);

      // Act
      final var resourceModel = ModelMapper.entityToModel(mockResource);

      // Assert
      assertEquals(mockResourceModel, resourceModel);
    }
  }

  @Test
  public void modelToEntity() {
    // Arrange
    final var mockResourceModel = mock(ResourceModel.class);
    final var mockResourceModelId = mock(UUID.class);
    final var mockResourceModelName = "mock-resource-model-name";
    final var mockResourceModelCreated = mock(ZonedDateTime.class);
    final var mockResourceModelUpdated = mock(ZonedDateTime.class);
    final var mockResourceModelDeleted = mock(ZonedDateTime.class);

    when(mockResourceModel.getId()).thenReturn(mockResourceModelId);
    when(mockResourceModel.getName()).thenReturn(mockResourceModelName);
    when(mockResourceModel.getCreated()).thenReturn(mockResourceModelCreated);
    when(mockResourceModel.getUpdated()).thenReturn(mockResourceModelUpdated);
    when(mockResourceModel.getDeleted()).thenReturn(mockResourceModelDeleted);

    final var constructResourceArgs = new HashMap<Resource, List<?>>();

    try (final var constructResource =
        mockConstruction(
            Resource.class, (mock, ctx) -> constructResourceArgs.put(mock, ctx.arguments()))) {

      // Act
      final var resource = ModelMapper.modelToEntity(mockResourceModel);

      // Assert
      assertEquals(1, constructResource.constructed().size());

      final var mockResource = constructResource.constructed().get(0);

      assertEquals(mockResourceModelId, constructResourceArgs.get(mockResource).get(0));
      assertEquals(mockResourceModelName, constructResourceArgs.get(mockResource).get(1));
      assertEquals(mockResourceModelCreated, constructResourceArgs.get(mockResource).get(2));
      assertEquals(mockResourceModelUpdated, constructResourceArgs.get(mockResource).get(3));

      verify(mockResource, times(1)).setDeleted(mockResourceModelDeleted);

      assertEquals(mockResource, resource);
    }
  }
}
