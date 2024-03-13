package com.atradius.utils;

import com.atradius.annotation.SetEventMetadata;
import com.atradius.model.Metadata;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.atradius.utils.HeaderConstants.EVENT_NAME;

public class EventMetadataConverter {

    public static Metadata toEventMetadata(Map<String, Object> headers) {
        var metadataBuilder = Metadata.builder();
        if (headers.containsKey(EVENT_NAME)) {
            String eventName = objectToString(headers.get(EVENT_NAME));
            metadataBuilder.eventName(eventName);
        }

        if (headers.containsKey(HeaderConstants.EVENT_DOMAIN)) {
            metadataBuilder.eventDomain(objectToString(headers.get(HeaderConstants.EVENT_DOMAIN)));
        }

        if (headers.containsKey(HeaderConstants.EVENT_SUBDOMAIN)) {
            metadataBuilder.eventSubdomain(objectToString(headers.get(HeaderConstants.EVENT_SUBDOMAIN)));
        }

        if (headers.containsKey(HeaderConstants.EVENT_TYPE)) {
            metadataBuilder.eventType(objectToString(headers.get(HeaderConstants.EVENT_TYPE)));
        }

        if (headers.containsKey(HeaderConstants.EVENT_CORRELATION_ID)) {
            String correlationId = objectToString(headers.get(HeaderConstants.EVENT_CORRELATION_ID));
            metadataBuilder.correlationId(Optional.of(UUID.fromString(correlationId)));
        }

        return metadataBuilder.build();
    }

    public static Metadata toEventMetadata(SetEventMetadata annotation) {
        return Metadata.builder()
                .eventName(annotation.eventName())
                .eventDomain(annotation.domain())
                .eventSubdomain(annotation.subdomain())
                .build();
    }

    public static String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String objectToString(Object obj) {
        if (obj instanceof String strObj){
            return strObj;
        } else if (obj instanceof byte[] byteObj) {
            bytesToString(byteObj);
        } else {
            return "";
        }
        return "";
    }

}
