package com.atradius.scheduled;

import com.atradius.services.KafkaRegistryService;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SchemaScheduler {

    private final KafkaRegistryService kafkaRegistryService;
    //private final AtomicInteger messageIndex = new AtomicInteger(0);

    @Scheduled(fixedRate = 3000)
    void getSchemas() throws Exception {
        var schema = kafkaRegistryService.getSchemaFromRegistry();
        kafkaRegistryService.compileSchema(schema.getNamespace());
    }
}
