package com.antwika.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public class LinkHeaderUtil {
  private LinkHeaderUtil() {}

  public static String format(String rel, String value) {
    return String.format("<%s>; rel=\"%s\"", value, rel);
  }

  public static String getCurrentMethodName() {
    return StackWalker.getInstance().walk(s -> s.skip(3).findFirst()).get().getMethodName();
  }

  public static Class<?> getCurrentClass() {
    try {
      final var className =
          StackWalker.getInstance().walk(s -> s.skip(3).findFirst()).get().getClassName();
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static Link getSelfLink(Object... params) {
    try {
      final Class<?>[] paramClasses =
          Arrays.stream(params).map(Object::getClass).toArray(Class[]::new);
      final var controller = getCurrentClass();
      final var proxy = WebMvcLinkBuilder.methodOn(controller);
      final var proxyClass = proxy.getClass();
      final var method = getCurrentMethodName();
      final Method proxyMethod = proxyClass.getMethod(method, paramClasses);
      final var proxyMethodResult = proxyMethod.invoke(proxy, params);
      return WebMvcLinkBuilder.linkTo(proxyMethodResult).withSelfRel();
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getLocationLink(Link link, String id) {
    link.getHref();
    return String.format(
        "%s/%s", link.getHref(), id);
  }

  public static HttpHeaders createHeadersWithLocation(String createdResourceId, Object... params) {
    final var headers = new HttpHeaders();

    final var link = LinkHeaderUtil.getSelfLink(params);
    headers.add(HttpHeaders.LINK, link.toString());

    headers.add(
        HttpHeaders.LOCATION,
        LinkHeaderUtil.getLocationLink(link, createdResourceId));

    headers.add(
        HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
        String.join(",", HttpHeaders.LINK, HttpHeaders.LOCATION));

    return headers;
  }

  public static HttpHeaders createHeaders(Object... params) {
    final var headers = new HttpHeaders();

    final var link = LinkHeaderUtil.getSelfLink(params);
    headers.add(HttpHeaders.LINK, link.toString());

    headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LINK);

    return headers;
  }

  public static <T> HttpHeaders createHeadersWithPagination(Page<T> page, Object... params) {
    final var headers = new HttpHeaders();

    final var accessControlExposeHeaders = new ArrayList<String>();

    final var selfLink = LinkHeaderUtil.getSelfLink(params);

    headers.add("X-Pagination-Page", Integer.toString(page.getNumber()));
    accessControlExposeHeaders.add("X-Pagination-Page");

    headers.add("X-Pagination-Limit", Integer.toString(page.getSize()));
    accessControlExposeHeaders.add("X-Pagination-Limit");

    headers.add("X-Pagination-Total", Integer.toString(page.getTotalPages()));
    accessControlExposeHeaders.add("X-Pagination-Total");

    headers.add("X-Pagination-Count", Long.toString(page.getContent().size()));
    accessControlExposeHeaders.add("X-Pagination-Count");

    final var links = new ArrayList<Link>();
    links.add(selfLink);

    if (page.getTotalPages() != 0) {
      final var first =
        Link.of(
            UriComponentsBuilder.fromUriString(selfLink.getHref())
                .replaceQueryParam("page", 0)
                .toUriString(),
            "first");
      links.add(first);

      if (!page.isFirst() && page.getNumber() < page.getTotalPages()) {
        final var prev =
            Link.of(
                UriComponentsBuilder.fromUriString(selfLink.getHref())
                    .replaceQueryParam("page", page.previousOrFirstPageable().getPageNumber())
                    .toUriString(),
                "prev");
        links.add(prev);
      }

      if (!page.isLast()) {
        final var next =
            Link.of(
                UriComponentsBuilder.fromUriString(selfLink.getHref())
                    .replaceQueryParam("page", page.nextOrLastPageable().getPageNumber())
                    .toUriString(),
                "next");
        links.add(next);
      }

      final var last =
          Link.of(
              UriComponentsBuilder.fromUriString(selfLink.getHref())
                  .replaceQueryParam("page", page.getTotalPages() - 1)
                  .toUriString(),
              "last");
      links.add(last);
    }

    headers.add(HttpHeaders.LINK, Links.of(links).toString());
    accessControlExposeHeaders.add(HttpHeaders.LINK);

    headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, String.join(",", accessControlExposeHeaders));

    return headers;
  }
}
