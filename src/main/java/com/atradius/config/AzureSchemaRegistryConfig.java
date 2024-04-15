package com.atradius.config;

import com.atradius.utils.SerDeUtils;
import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Map;

/**
 * Utility class to reuse the Schema Registry configuration in the Kafka producer and consumer definition
 * */
final class AzureSchemaRegistryConfig {
    static final String AZURE_ENVIRONMENT = "azure";

    /**
     * Property to set an Azure Credential to interact with the Azure Schema Registry with the Azure Managed Identities
     * */
    private static final String SCHEMA_REGISTRY_CREDENTIAL_CONFIG = "schema.registry.credential";

    private AzureSchemaRegistryConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Set the Azure Schema Registry properties to interact with the Azure Schema Registry
     * The Azure Schema Registry only works with a RecordNameStrategy, even if you change other strategies in the configuration.
     * This means name of the schemas in the Schema Registry should follow the pattern: ${namespace}.${name}
     * @param config the configuration map
     */
    static void setAzureSchemaRegistryConfig(Map<String, Object> config) {
        TokenCredential tokenCredential = new DefaultAzureCredentialBuilder().build();

        config.put(AbstractKafkaSchemaSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
        config.put(SCHEMA_REGISTRY_CREDENTIAL_CONFIG, tokenCredential);
    }

    static void setAzureSerializersConfig(Map<String, Object> config) {
        var currentValueSerializer = config.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG);

        if (SerDeUtils.isDefaultSerializer(currentValueSerializer)){
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    com.microsoft.azure.schemaregistry.kafka.avro.KafkaAvroSerializer.class);
        }
    }

    static void setAzureDeserializersConfig(Map<String, Object> config) {
        var currentValueDeserializer = config.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);

        if (SerDeUtils.isDefaultDeserializer(currentValueDeserializer)){
            config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    com.microsoft.azure.schemaregistry.kafka.avro.KafkaAvroDeserializer.class);
        }
    }
}
