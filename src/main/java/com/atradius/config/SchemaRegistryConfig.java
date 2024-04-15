package com.atradius.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;

import java.util.Map;

import static com.atradius.config.AzureSchemaRegistryConfig.AZURE_ENVIRONMENT;

/**
 * Utility class to reuse the Schema Registry configuration in the Kafka producer and consumer definition
 * */
final class SchemaRegistryConfig {

    private SchemaRegistryConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Set the default configuration for the Schema Registry
     * @param config the configuration map
     */
    static void setSchemaRegistryDefaultConfig(Map<String, Object> config) {
        config.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, false);
        config.put(AbstractKafkaSchemaSerDeConfig.USE_LATEST_VERSION, true);
    }

    /**
     * Set the configuration for the Schema Registry based on the cloud environment
     * @param cloudEnvironment the cloud environment (azure or confluent)
     * @param config the configuration map
     */
    static void setSchemaRegistryConfigByEnvironment(String cloudEnvironment, Map<String, Object> config) {
        if (cloudEnvironment.equals(AZURE_ENVIRONMENT)) {
            AzureSchemaRegistryConfig.setAzureSchemaRegistryConfig(config);
        } else {
            ConfluentSchemaRegistryConfig.setConfluentSchemaRegistryConfig(config);
        }
    }

    /**
     * Set the serializers configuration for the Schema Registry based on the cloud environment
     * @param cloudEnvironment the cloud environment (azure or confluent)
     * @param config the configuration map
     */
    static void setSerializersConfigByEnvironment(String cloudEnvironment, Map<String, Object> config) {
        if (cloudEnvironment.equals(AZURE_ENVIRONMENT)) {
            AzureSchemaRegistryConfig.setAzureSerializersConfig(config);
        } else {
            ConfluentSchemaRegistryConfig.setConfluentSerializersConfig(config);
        }
    }

    /**
     * Set the deserializers configuration for the Schema Registry based on the cloud environment
     * @param cloudEnvironment the cloud environment (azure or confluent)
     * @param config the configuration map
     */
    static void setDeserializersConfigByEnvironment(String cloudEnvironment, Map<String, Object> config) {
        if (cloudEnvironment.equals(AZURE_ENVIRONMENT)) {
            AzureSchemaRegistryConfig.setAzureDeserializersConfig(config);
        } else {
            ConfluentSchemaRegistryConfig.setConfluentDeserializersConfig(config);
        }
    }
}
