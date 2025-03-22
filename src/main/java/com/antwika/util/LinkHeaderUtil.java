package com.antwika.util;

public class LinkHeaderUtil {
  private LinkHeaderUtil() {}

  public static String format(String rel, String value) {
    return String.format("<%s>; rel=\"%s\"", value, rel);
  }
}
