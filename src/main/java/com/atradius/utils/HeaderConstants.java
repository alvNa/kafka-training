package com.atradius.utils;

public final class HeaderConstants {
  private HeaderConstants() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static final String EVENT_NAME = "event-name";
  public static final String EVENT_DOMAIN = "event-domain";
  public static final String EVENT_SUBDOMAIN = "event-subdomain";
  public static final String EVENT_TYPE = "event-type";
  public static final String ACTION_TYPE = "action-type";
  public static final String EVENT_CORRELATION_ID = "correlation-id";
}
