package com.atradius.handler;

import com.atradius.annotation.GetEventMetadata;
import com.atradius.examples.Message;
import com.atradius.model.Metadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.atradius.utils.EventMetadataConverter.toEventMetadata;


@Component
@Slf4j
public class MessageHandler {

//    @KafkaListener(topics = "${spring.kafka.topics}", groupId = "consumer-1")
//    void consumeMessagesGroup1(final Message msg, @Headers Map<String, Object> headers) {
//        var eventMetadata = toEventMetadata(headers);
//        log.info("Message received: {}", msg);
//        log.info("Received event metadata: {}", eventMetadata);
//    }

    @KafkaListener(topics = "${spring.kafka.topics}", groupId = "consumer-2")
    void consumeMessagesGroup2(final Message msg) {
        log.info("Message received: {}", msg);
        //log.info("Received event metadata: {}", eventMetadata);
    }
}
