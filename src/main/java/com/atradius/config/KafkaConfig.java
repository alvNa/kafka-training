package com.atradius.config;

import com.azure.core.credential.TokenCredential;
import com.azure.data.schemaregistry.SchemaRegistryAsyncClient;
import com.azure.data.schemaregistry.SchemaRegistryClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import static com.microsoft.azure.schemaregistry.kafka.avro.KafkaAvroSerializerConfig.SCHEMA_REGISTRY_CREDENTIAL_CONFIG;


import java.util.Map;

@Configuration
public class KafkaConfig<K, V> {
    public static final String AVRO_PRODUCER_FACTORY = "avroProducerFactory";

    @Value("${app.eventhub.namespace}")
    private String eventHubNamespace;


    @Primary
    @Bean(name = AVRO_PRODUCER_FACTORY)
    @ConditionalOnMissingBean(name = AVRO_PRODUCER_FACTORY)
    public ProducerFactory<K, V> avroProducerFactory(final KafkaProperties properties) {
        final Map<String, Object> config = properties.buildProducerProperties();
        config.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, false);
        config.put(AbstractKafkaSchemaSerDeConfig.USE_LATEST_VERSION, true);
        config.put(
                AbstractKafkaSchemaSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY,
                io.confluent.kafka.serializers.subject.TopicNameStrategy.class);
        //Set the default token credential for apps configured with the Azure Managed Identities
        TokenCredential tokenCredential = new DefaultAzureCredentialBuilder().build();
        config.put(SCHEMA_REGISTRY_CREDENTIAL_CONFIG, tokenCredential);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public SchemaRegistryAsyncClient schemaRegistryClient() {
        return new SchemaRegistryClientBuilder()
                .fullyQualifiedNamespace(eventHubNamespace + ".servicebus.windows.net")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildAsyncClient();
    }
}
