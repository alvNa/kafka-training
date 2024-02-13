package com.atradius.scheduled;

import com.atradius.examples.Message;
import com.atradius.services.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class EventScheduler {

    private final ProducerService producerService;
    private final AtomicInteger messageIndex = new AtomicInteger(0);

    @Scheduled(fixedRate = 3000)
    void sendMessages() {
        var msg = new Message(messageIndex.get(), "Hello world " + messageIndex.getAndIncrement() + " !");
        producerService.send(msg);
    }
}
