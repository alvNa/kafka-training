package com.atradius.utils;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Utility class for serialization and deserialization
 * */
public final class SerDeUtils {
    private SerDeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Check if the current serializer is the default one in Kafka (StringSerializer)
     * @param currentSerializer the current serializer
     * @return true if the current serializer is the default one
     */
    public static boolean isDefaultSerializer(Object currentSerializer) {
        return isDefaultSerDe(currentSerializer, StringSerializer.class);
    }

    /**
     * Check if the current deserializer is the default one in Kafka (StringDeserializer)
     * @param currentDeserializer the current deserializer
     * @return true if the current deserializer is the default one
     */
    public static boolean isDefaultDeserializer(Object currentDeserializer) {
        return isDefaultSerDe(currentDeserializer, StringDeserializer.class);
    }

    private static boolean isDefaultSerDe(Object currentDerDe, Class<?> defaultSerDe) {
        return currentDerDe.equals(defaultSerDe);
    }
}
