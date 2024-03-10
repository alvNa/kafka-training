package com.atradius.handler;

import com.atradius.examples.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.atradius.utils.MetadataUtils.getCustomMetadata;

@Component
@Slf4j
public class MessageHandler {

    @KafkaListener(topics = "${spring.kafka.topics}")
    void processPosition(final Message msg,
                         @Headers Map<String, Object> headers//,
    //                   @EventMetadata Metadata eventCustomMetadata
    ) {
        var eventCustomMetadata = getCustomMetadata(headers);
        log.info("Message received: {}", msg);
        log.info("Received event metadata: {}", eventCustomMetadata);
    }
}
