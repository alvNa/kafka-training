package com.atradius.config;

import com.atradius.producer.EventMetadataInterceptor;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@EnableAspectJAutoProxy
@Configuration
public class KafkaConfig<K,V> {
    /** Bean name of Event Library Event Kafka Template */
    public static final String AVRO_KAFKA_TEMPLATE = "avroKafkaTemplate";
    public static final String AVRO_PRODUCER_FACTORY = "avroProducerFactory";

    @Bean(name = AVRO_PRODUCER_FACTORY)
    @ConditionalOnMissingBean(name = AVRO_PRODUCER_FACTORY)
    @SuppressWarnings("java:S1452")
    public ProducerFactory<K, V> avroProducerFactory(final KafkaProperties properties) {
        final Map<String, Object> config = properties.buildProducerProperties();
        config.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, false);
        config.put(KafkaAvroSerializerConfig.USE_LATEST_VERSION, true);
        config.put(
                KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY,
                io.confluent.kafka.serializers.subject.TopicNameStrategy.class);
        config.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, EventMetadataInterceptor.class.getName());

        var producerFactory = new DefaultKafkaProducerFactory<K, V>(config);
       // producerFactory.set
        return producerFactory;
    }
    @Bean(name = AVRO_KAFKA_TEMPLATE)
    @ConditionalOnMissingBean(name = AVRO_KAFKA_TEMPLATE)
    public KafkaTemplate<K, V> avroKafkaTemplate(
            final KafkaProperties properties,
            /*@Qualifier(AVRO_PRODUCER_FACTORY)*/ final ProducerFactory<K, V> kafkaProducerFactory,
            EventMetadataInterceptor eventMetadataInterceptor) {
        final PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();

        final KafkaTemplate<K, V> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        map.from(properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(properties.getTemplate().getTransactionIdPrefix())
                .to(kafkaTemplate::setTransactionIdPrefix);

        kafkaTemplate.setObservationEnabled(true);
        kafkaTemplate.setProducerInterceptor(eventMetadataInterceptor);

        return kafkaTemplate;
    }

  public static final String KAFKA_TEMPLATE_INTERCEPTOR = "kafkaTemplateInterceptor";

  @Bean(name = KAFKA_TEMPLATE_INTERCEPTOR)
  public EventMetadataInterceptor eventMetadataInterceptor() {
    return new EventMetadataInterceptor();
  }

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<K,V> kafkaListenerContainerFactory(final KafkaProperties properties) {
//
//        ConcurrentKafkaListenerContainerFactory<K,V> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory(properties));
//        //factory.setRecordMessageConverter(customConverter());
//        return factory;
//    }

//    @Bean
//    public ConsumerFactory<K, V> consumerFactory(final KafkaProperties properties) {
//        Map<String, Object> props = properties.buildProducerProperties();
//        //props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "message-consumer");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }

//    @Bean
//    public RecordMessageConverter customConverter() {
//        return new CustomMessageConverter();
//    }
}
