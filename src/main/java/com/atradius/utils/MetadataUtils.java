package com.atradius.utils;

import com.atradius.model.Metadata;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class MetadataUtils {
  private MetadataUtils() {}

  public static <K, V> ProducerRecord<K, V> addCustomMetadata(
      final ProducerRecord<K, V> record, final Metadata metadata) {
    record.headers().add(HeaderConstants.EVENT_NAME, metadata.eventName().getBytes());
    record.headers().add(HeaderConstants.EVENT_DOMAIN, metadata.eventDomain().getBytes());
    record.headers().add(HeaderConstants.EVENT_TYPE, metadata.eventType().getBytes());
    String correlationId = computeCorrelationIdIfAbsent(metadata.correlationId()).toString();
    record.headers().add(HeaderConstants.EVENT_CORRELATION_ID, correlationId.getBytes());

    return record;
  }

  public static Metadata getCustomMetadata(Map<String, Object> headers) {
    var metadataBuilder = Metadata.builder();
    if (headers.containsKey(HeaderConstants.EVENT_NAME)) {
      String eventName = bytesToString((byte[])headers.get(HeaderConstants.EVENT_NAME));
      metadataBuilder.eventName(eventName);
    }

    if (headers.containsKey(HeaderConstants.EVENT_DOMAIN)) {
      metadataBuilder.eventDomain(bytesToString((byte[])headers.get(HeaderConstants.EVENT_DOMAIN)));
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
