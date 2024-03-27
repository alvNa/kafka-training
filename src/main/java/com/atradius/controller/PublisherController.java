package com.atradius.controller;

import com.azure.data.schemaregistry.SchemaRegistryAsyncClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublisherController {

    @Value("${spring.kafka.topics}")
    private String topicName;

    @Value("${app.eventhub.schema-registry.group}")
    private String groupName;

    private final SchemaRegistryAsyncClient schemaRegistryClient;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        log.info("Hello endpoint hit!");
        log.info(schemaRegistryClient.getFullyQualifiedNamespace());
        log.info(
            Objects.requireNonNull(
                    schemaRegistryClient
                        .getSchema(groupName, topicName + "-value", 1)
                        .block())
                .getDefinition());
        log.info(
            Objects.requireNonNull(
                    schemaRegistryClient
                        .getSchema(groupName, topicName, 1)
                        .block())
                .getDefinition());
        return ResponseEntity.ok("hello");
    }
}
