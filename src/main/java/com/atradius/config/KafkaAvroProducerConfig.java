package com.atradius.config;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;


/**
 * Spring Boot autoconfiguration for Event Model Library.
 *
 * <p>Defined beans for a ProducerFactory for Avro. The avro configuration is
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
public class KafkaAvroProducerConfig<K, V extends SpecificRecordBase> {
  /** Bean name of Event Library Event Producer Factory */
  public static final String AVRO_PRODUCER_FACTORY = "avroProducerFactory";

  /** Bean name of Event Library Event Kafka Template */
  public static final String AVRO_KAFKA_TEMPLATE = "avroKafkaTemplate";

  @Value("${andromeda.events.cloud.environment:confluent}")
  private String cloudEnvironment;

  /**
   * Create a Kafka producer factory for avro models
   *
   * @param properties the Kafka properties
   * @return the Kafka producer factory for envelopes
   */
  @Bean(name = AVRO_PRODUCER_FACTORY)
  @ConditionalOnMissingBean(name = AVRO_PRODUCER_FACTORY)
  public ProducerFactory<K, V> avroProducerFactory(final KafkaProperties properties) {
    final Map<String, Object> config = properties.buildProducerProperties();

    SchemaRegistryConfig.setSchemaRegistryDefaultConfig(config);
    SchemaRegistryConfig.setSchemaRegistryConfigByEnvironment(cloudEnvironment, config);
    SchemaRegistryConfig.setSerializersConfigByEnvironment(cloudEnvironment, config);

    return new DefaultKafkaProducerFactory<>(config);
  }

  /**
   * Create a Kafka template
   *
   * @param properties the Kafka properties
   * @param kafkaProducerFactory the Kafka producer factory
   * @return a Kafka template
   */
  @Bean(name = AVRO_KAFKA_TEMPLATE)
  @ConditionalOnMissingBean(name = AVRO_KAFKA_TEMPLATE)
  public KafkaTemplate<K, V> avroKafkaTemplate(
      final KafkaProperties properties,
      @Qualifier(AVRO_PRODUCER_FACTORY) final ProducerFactory<K, V> kafkaProducerFactory){
    final PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();

    final KafkaTemplate<K, V> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
    map.from(properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
    map.from(properties.getTemplate().getTransactionIdPrefix())
        .to(kafkaTemplate::setTransactionIdPrefix);

    kafkaTemplate.setObservationEnabled(true);

    return kafkaTemplate;
  }
}
