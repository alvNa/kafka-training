package com.atradius.utils;

import com.atradius.model.Metadata;

import java.util.Optional;
import java.util.UUID;

import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;

import static com.atradius.utils.EventMetadataConverter.bytesToString;
import static com.atradius.utils.HeaderConstants.*;
import static java.util.Objects.nonNull;

public final class MetadataUtils {

  public static final String WARN_MESSAGE = "Don't forget to add it to the custom event metadata using the kafka headers";
  public static final String HEADER_NOT_FOUND = "{} not found!";

  private MetadataUtils() {}

  public static void addEventMetadata(Headers headers, final Metadata metadata) {
    if (nonNull(headers) && nonNull(metadata)) {
      if (nonNull(metadata.eventName())) {
        headers.add(EVENT_NAME, metadata.eventName().getBytes());
      }
      if (nonNull(metadata.eventDomain())) {
        headers.add(EVENT_DOMAIN, metadata.eventDomain().getBytes());
      }
      if (nonNull(metadata.eventSubdomain())) {
        headers.add(EVENT_SUBDOMAIN, metadata.eventSubdomain().getBytes());
      }
//    record.headers().add(HeaderConstants.EVENT_TYPE, metadata.eventType().getBytes());
//    String correlationId = computeCorrelationIdIfAbsent(metadata.correlationId()).toString();
//    record.headers().add(HeaderConstants.EVENT_CORRELATION_ID, correlationId.getBytes());

      //return record;
    }
  }

  public static void printEventMetadataLogs(Logger log, Headers headers) {
    var eventNameIterator = headers.headers(EVENT_NAME).iterator();
    if (!eventNameIterator.hasNext()) {
      log.warn(HEADER_NOT_FOUND + WARN_MESSAGE, EVENT_NAME);
    }
    else{
      var eventName = bytesToString(eventNameIterator.next().value());
      log.info("Event Name is {}", eventName);
    }
  }



  private static UUID computeCorrelationIdIfAbsent(Optional<UUID> maybeCorrelationId) {
    return maybeCorrelationId.orElseGet(UUID::randomUUID);
  }
}
