package com.atradius.utils;

import com.atradius.model.Metadata;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;

import static com.atradius.utils.HeaderConstants.EVENT_NAME;

public final class MetadataUtils {

  public static final String WARN_MESSAGE = "Don't forget to add it to the custom event metadata using the kafka headers";
  public static final String HEADER_NOT_FOUND = "{} not found!";

  private MetadataUtils() {}

  public static <K, V> ProducerRecord<K, V> addCustomMetadata(
      final ProducerRecord<K, V> record, final Metadata metadata) {
    record.headers().add(EVENT_NAME, metadata.eventName().getBytes());
    record.headers().add(HeaderConstants.EVENT_DOMAIN, metadata.eventDomain().getBytes());
    record.headers().add(HeaderConstants.EVENT_SUBDOMAIN, metadata.eventSubdomain().getBytes());
//    record.headers().add(HeaderConstants.EVENT_TYPE, metadata.eventType().getBytes());
//    String correlationId = computeCorrelationIdIfAbsent(metadata.correlationId()).toString();
//    record.headers().add(HeaderConstants.EVENT_CORRELATION_ID, correlationId.getBytes());

    return record;
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

  public static Metadata getCustomMetadata(Map<String, Object> headers) {
    var metadataBuilder = Metadata.builder();
    if (headers.containsKey(EVENT_NAME)) {
      String eventName = bytesToString((byte[])headers.get(EVENT_NAME));
      metadataBuilder.eventName(eventName);
    }

    if (headers.containsKey(HeaderConstants.EVENT_DOMAIN)) {
      metadataBuilder.eventDomain(bytesToString((byte[])headers.get(HeaderConstants.EVENT_DOMAIN)));
    }

    if (headers.containsKey(HeaderConstants.EVENT_SUBDOMAIN)) {
      metadataBuilder.eventSubdomain(bytesToString((byte[])headers.get(HeaderConstants.EVENT_SUBDOMAIN)));
    }

    if (headers.containsKey(HeaderConstants.EVENT_TYPE)) {
      metadataBuilder.eventType(bytesToString((byte[])headers.get(HeaderConstants.EVENT_TYPE)));
    }

    if (headers.containsKey(HeaderConstants.EVENT_CORRELATION_ID)) {
      String correlationId = bytesToString((byte[])headers.get(HeaderConstants.EVENT_CORRELATION_ID));
      metadataBuilder.correlationId(Optional.of(UUID.fromString(correlationId)));
    }

    return metadataBuilder.build();
  }

  private static UUID computeCorrelationIdIfAbsent(Optional<UUID> maybeCorrelationId) {
    return maybeCorrelationId.orElseGet(UUID::randomUUID);
  }

  private static String bytesToString(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
