package com.antwika.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class LinkHeaderUtil {
  private LinkHeaderUtil() {}

  public static String format(String rel, String value) {
    return String.format("<%s>; rel=\"%s\"", value, rel);
  }

  public static String getCurrentMethodName() {
    return StackWalker.getInstance().walk(s -> s.skip(2).findFirst()).get().getMethodName();
  }

  public static Class<?> getCurrentClass() {
    try {
      final var className =
          StackWalker.getInstance().walk(s -> s.skip(2).findFirst()).get().getClassName();
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getSelfLink(Object... params) {
    try {
      final Class<?>[] paramClasses =
          Arrays.stream(params).map(Object::getClass).toArray(Class[]::new);
      final var controller = getCurrentClass();
      final var proxy = WebMvcLinkBuilder.methodOn(controller);
      final var proxyClass = proxy.getClass();
      final var method = getCurrentMethodName();
      final Method proxyMethod = proxyClass.getMethod(method, paramClasses);
      final var proxyMethodResult = proxyMethod.invoke(proxy, params);
      return WebMvcLinkBuilder.linkTo(proxyMethodResult).withSelfRel().toString();
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
