package com.antwika.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LinkHeaderUtilTest {
  @Test
  public void format() {
    assertEquals(
        "<http://localhost:8080/v0/example>; rel=\"example\"",
        LinkHeaderUtil.format("example", "http://localhost:8080/v0/example"));
  }
}
