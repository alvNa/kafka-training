package com.atradius.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

/**
 * Spring Boot autoconfiguration for Event Model Library.
 *
 * <p>Defined beans for a ConsumerFactory for Avro. The avro configuration is
 * the primary default configuration
 *
 * <p>In the version 0.3.0 we set a conditional property to enable the autoconfiguration of the Avro
 * model. By default, is set to false in order to not breaking previous developments with version
 * 0.2.x. In future versions, this property will be set to true by default.
 *
 * @param <K> generic type for the key
 * @param <V> generic type for the value, which extends from an AVRO object
 */
@AutoConfiguration
@ConditionalOnProperty(value = "andromeda.events.avro.enabled", havingValue = "true")
public class KafkaAvroConsumerConfig<K, V extends SpecificRecordBase> {
    /** Bean name of Event Library Kafka Listener Container Factory */
    public static final String AVRO_LISTENER_CONTAINER_FACTORY = "avroListenerContainerFactory";

    /** Bean name of Event Library Kafka Event Consumer Factory */
    public static final String AVRO_CONSUMER_FACTORY = "avroConsumerFactory";

    @Value("${andromeda.events.cloud.environment:confluent}")
    private String cloudEnvironment;

    /**
     * Create a concurrent Kafka listener container factory
     *
     * @param consumerFactory the envelope consumer factory
     * @return a concurrent Kafka listener container factory
     */
    @Bean(name = AVRO_LISTENER_CONTAINER_FACTORY)
    @ConditionalOnMissingBean(name = AVRO_LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<K, V> listenerContainerFactory(
            @Qualifier(AVRO_CONSUMER_FACTORY) final ConsumerFactory<K, V> consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<K, V> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    /**
     * Create a Kafka consumer factory for avro
     *
     * @param properties the Kafka properties
     * @return the Kafka consumer factory for envelopes+
     */
    @Bean(name = AVRO_CONSUMER_FACTORY)
    @ConditionalOnMissingBean(name = AVRO_CONSUMER_FACTORY)
    public ConsumerFactory<K, V> consumerFactory(final KafkaProperties properties) {
        final Map<String, Object> config = properties.buildConsumerProperties();

        SchemaRegistryConfig.setSchemaRegistryDefaultConfig(config);
        config.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        SchemaRegistryConfig.setSchemaRegistryConfigByEnvironment(cloudEnvironment, config);
        SchemaRegistryConfig.setDeserializersConfigByEnvironment(cloudEnvironment, config);

        return new DefaultKafkaConsumerFactory<>(config);
    }
}
