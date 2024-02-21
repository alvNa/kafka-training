package com.atradius.config;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public SchemaRegistryClient schemaRegistryClient(@Value("${spring.kafka.properties.schema.registry.url}") String baseUrl){
        //ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
        //client.setEndpoint(endpoint);
        //return client;
        SchemaRegistryClient client = new CachedSchemaRegistryClient(baseUrl,16);
        return client;
    }
}
