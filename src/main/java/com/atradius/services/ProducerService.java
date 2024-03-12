package com.atradius.services;

import com.atradius.examples.Message;
import com.atradius.annotation.SetEventMetadata;
import com.atradius.model.Metadata;
import com.atradius.utils.ActionTypesConstants;
import com.atradius.utils.DomainConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

import static com.atradius.utils.EventNames.BUYER_PI_READY;
import static com.atradius.utils.HeaderConstants.*;
import static com.atradius.utils.MetadataUtils.addEventMetadata;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    @Value("${spring.kafka.topics}")
    private String topicName;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public Future<SendResult<String, Message>> send(Message value) {
        var producerRecord = new ProducerRecord<String,Message>(topicName, null, value);
        var metadata = getMetadata();
        addEventMetadata(producerRecord.headers(), metadata);
        return kafkaTemplate.send(producerRecord);
    }

    @SetEventMetadata(eventName = "my-event-updated", domain = "sc2", subdomain = "platform2")
    public Future<SendResult<String, Message>> send2(Message value) {
        var producerRecord = new ProducerRecord<String,Message>(topicName, null, value);
        return kafkaTemplate.send(producerRecord);
    }

    public Future<SendResult<String, Message>> send3(Message value) {
        org.springframework.messaging.Message<Message> message = MessageBuilder
                .withPayload(value)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(EVENT_NAME, BUYER_PI_READY)
                .setHeader(EVENT_DOMAIN, DomainConstants.CREDIT_INSURANCE)
                .setHeader(EVENT_SUBDOMAIN, ActionTypesConstants.NOTIFICATION)
                .build();

        return kafkaTemplate.send(message);
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
