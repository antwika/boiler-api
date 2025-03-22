package com.antwika;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class MainTest {
  @Test
  public void construct() {
    new Main();
  }

  @Test
  public void testMain() {
    // Arrange
    final var mockLogger = mock(Logger.class);
    final var mockArgs = new String[] {"foo", "bar"};

    try (final var staticSpringApplication = mockStatic(SpringApplication.class);
        final var staticLoggerFactory = mockStatic(LoggerFactory.class)) {

      staticLoggerFactory.when(() -> LoggerFactory.getLogger(Main.class)).thenReturn(mockLogger);

      // Act
      Main.main(mockArgs);

      // Assert
      staticSpringApplication.verify(() -> SpringApplication.run(Main.class, mockArgs), times(1));
      // verify(mockLogger, times(1)).info("Application started");
    }
  }
}
