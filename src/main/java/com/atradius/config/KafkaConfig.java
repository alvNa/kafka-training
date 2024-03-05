package com.atradius.config;

import com.atradius.aop.EventMetadataInterceptor;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaConfig {
    /** Bean name of Event Library Event Kafka Template */
    public static final String AVRO_KAFKA_TEMPLATE = "avroKafkaTemplate";
    public static final String AVRO_PRODUCER_FACTORY = "avroProducerFactory";

    @Bean(name = AVRO_PRODUCER_FACTORY)
    @ConditionalOnMissingBean(name = AVRO_PRODUCER_FACTORY)
    @SuppressWarnings("java:S1452")
    public <K, V> ProducerFactory<K, V> avroProducerFactory(final KafkaProperties properties) {
        final Map<String, Object> config = properties.buildProducerProperties();
        config.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, false);
        config.put(KafkaAvroSerializerConfig.USE_LATEST_VERSION, true);
        config.put(
                KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY,
                io.confluent.kafka.serializers.subject.TopicNameStrategy.class);
        config.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, EventMetadataInterceptor.class.getName());

        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean(name = AVRO_KAFKA_TEMPLATE)
    @ConditionalOnMissingBean(name = AVRO_KAFKA_TEMPLATE)
    public <K, V> KafkaTemplate<K, V> avroKafkaTemplate(
            final KafkaProperties properties,
            /*@Qualifier(AVRO_PRODUCER_FACTORY)*/ final ProducerFactory<K, V> kafkaProducerFactory) {
        final PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();

        final KafkaTemplate<K, V> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        map.from(properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(properties.getTemplate().getTransactionIdPrefix())
                .to(kafkaTemplate::setTransactionIdPrefix);

        kafkaTemplate.setObservationEnabled(true);

        return kafkaTemplate;
    }
}
