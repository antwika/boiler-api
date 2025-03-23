package com.antwika.exception;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class ResourceNotFoundExceptionTest {
  @Test
  public void construct() {
    assertDoesNotThrow(ResourceNotFoundException::new);
  }
}
