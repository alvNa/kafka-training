package com.atradius.services;

import com.atradius.examples.Message;
import com.atradius.handler.annotation.SetEventMetadata;
import com.atradius.model.Metadata;
import com.atradius.utils.ActionTypesConstants;
import com.atradius.utils.DomainConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

import static com.atradius.utils.EventNames.BUYER_PI_READY;
import static com.atradius.utils.MetadataUtils.addCustomMetadata;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    @Value("${spring.kafka.topics}")
    private String topicName;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @SetEventMetadata(eventName = "my-event-created", domain = "sc", subdomain = "platform")
    public Future<SendResult<String, Message>> send(Message value) {
        var producerRecord = new ProducerRecord<String,Message>(topicName, null, value);
        var metadata = getMetadata();
        //addCustomMetadata(producerRecord, metadata);
        return kafkaTemplate.send(producerRecord);
    }

    private static Metadata getMetadata() {
        return Metadata.builder()
                .eventName(BUYER_PI_READY)
                .eventDomain(DomainConstants.CREDIT_INSURANCE)
                .eventType(ActionTypesConstants.NOTIFICATION)
                .correlationId(Optional.of(UUID.randomUUID()))
                .build();
    }
}
