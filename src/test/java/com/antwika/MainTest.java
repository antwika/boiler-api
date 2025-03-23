package com.antwika;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

public class MainTest {
  @Test
  public void construct() {
    new Main();
  }

  @Test
  public void testMain() {
    // Arrange
    final var mockArgs = new String[] {"foo", "bar"};

    try (final var staticSpringApplication = mockStatic(SpringApplication.class)) {

      // Act
      Main.main(mockArgs);

      // Assert
      staticSpringApplication.verify(() -> SpringApplication.run(Main.class, mockArgs), times(1));
    }
  }
}
