package com.atradius.services;

import com.atradius.examples.Tweet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    @Value("${spring.kafka.topics}")
    private String topicName;

    private final KafkaTemplate<String, Tweet> kafkaTemplate;

    public void send(Tweet value) {
        kafkaTemplate.send(topicName, value);
    }
}
