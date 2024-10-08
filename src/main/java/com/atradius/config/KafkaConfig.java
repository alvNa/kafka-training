package com.atradius.config;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaConfig<K, V> {
    public static final String AVRO_PRODUCER_FACTORY = "avroProducerFactory";

    @Value("${app.eventhub.namespace}")
    private String eventHubNamespace;


    @Primary
    @Bean(name = AVRO_PRODUCER_FACTORY)
    @ConditionalOnMissingBean(name = AVRO_PRODUCER_FACTORY)
    public ProducerFactory<K, V> producerFactory(final KafkaProperties properties) {
        final Map<String, Object> config = properties.buildProducerProperties();
        config.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, false);
        config.put(AbstractKafkaSchemaSerDeConfig.USE_LATEST_VERSION, true);
        config.put(
                AbstractKafkaSchemaSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY,
                io.confluent.kafka.serializers.subject.TopicNameStrategy.class);

        return new DefaultKafkaProducerFactory<>(config);

        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stateless-transformations-example");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        // Since the input topic uses Strings for both key and value, set the default Serdes to String.
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    }

}
