package com.atradius.handler;

import com.atradius.annotation.GetEventMetadata;
import com.atradius.examples.Message;
import com.atradius.model.Metadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.atradius.utils.EventMetadataConverter.toEventMetadata;
import static com.atradius.utils.HeaderConstants.*;


@Component
@Slf4j
public class MessageHandler {

    @KafkaListener(topics = "${spring.kafka.topics}", groupId = "consumer-1")
    void receiveGroup1(final Message msg, @Headers Map<String, Object> headers) {
        var eventMetadata = toEventMetadata(headers);
        log.info("Message received: {}", msg);
        log.info("Received event metadata: {}", eventMetadata);
    }

    @KafkaListener(topics = "${spring.kafka.topics}", groupId = "consumer-2")
    public void receiveGroup2(@Payload Message msg,
                        @Header(EVENT_NAME) String eventName,
                        @Header(EVENT_DOMAIN) String domain,
                        @Header(EVENT_SUBDOMAIN) String subdomain,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        //@Header(KafkaHeaders.PARTITION) String partition,
                        @Header(KafkaHeaders.OFFSET) String offset,
                        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Message received: {}", msg);
        log.info("Received event name: {}", eventName);
        log.info("Received event domain: {}", domain);
        log.info("Received event subdomain: {}", subdomain);
        log.info("Received event topic: {}", topic);
        //log.info("Received event partition: {}", partition);
        log.info("Received event offset: {}", offset);
        log.info("Received event offset: {}", timestamp);
    }

//    @KafkaListener(topics = "${spring.kafka.topics}", groupId = "consumer-2")
//    void consumeMessagesGroup2(final Message msg, @Headers Map<String, Object> headers,
//                               @GetEventMetadata Metadata eventMetadata) {
//        log.info("Message received: {}", msg);
//        log.info("Received event metadata: {}", eventMetadata);
//    }
}
