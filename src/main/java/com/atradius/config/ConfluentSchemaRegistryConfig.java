package com.atradius.config;

import com.atradius.utils.SerDeUtils;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Map;

/**
 * Utility class to reuse the Schema Registry configuration in the Kafka producer and consumer definition
 * */
final class ConfluentSchemaRegistryConfig {
    static final String CONFLUENT_ENVIRONMENT = "confluent";

    private ConfluentSchemaRegistryConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Set the Confluent Schema Registry properties to interact with the Confluent Schema Registry
     * Only set the default RecordNameStrategy if no other value has been set in the configuration
     * @param config the configuration map
     */
    static void setConfluentSchemaRegistryConfig(Map<String, Object> config) {
        config.putIfAbsent(AbstractKafkaSchemaSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
    }

    static void setConfluentSerializersConfig(Map<String, Object> config) {
        var currentValueSerializer = config.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG);

        if (SerDeUtils.isDefaultSerializer(currentValueSerializer)){
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        }
    }

    static void setConfluentDeserializersConfig(Map<String, Object> config) {
        var currentValueDeserializer = config.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);

        if (SerDeUtils.isDefaultDeserializer(currentValueDeserializer)){
            config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        }
    }
}
