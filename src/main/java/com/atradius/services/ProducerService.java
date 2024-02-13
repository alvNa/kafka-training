package com.atradius.services;

import com.atradius.examples.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    @Value("${spring.kafka.topics}")
    private String topicName;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public Future<SendResult<String, Message>> send(Message value) {
        return kafkaTemplate.send(topicName, value);
    }
}
