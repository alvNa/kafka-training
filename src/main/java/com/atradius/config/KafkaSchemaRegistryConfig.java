package com.atradius.config;

import com.azure.data.schemaregistry.SchemaRegistryAsyncClient;
import com.azure.data.schemaregistry.SchemaRegistryClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaSchemaRegistryConfig {

    @Value("${app.eventhub.namespace}")
    private String eventHubNamespace;


    @Bean
    public SchemaRegistryAsyncClient schemaRegistryClient() {
        return new SchemaRegistryClientBuilder()
                .fullyQualifiedNamespace(eventHubNamespace + ".servicebus.windows.net")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildAsyncClient();
    }
}
