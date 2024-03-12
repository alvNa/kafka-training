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
//        var msg = new Message(messageIndex.get(), "Hello world" + messageIndex.getAndIncrement() + " !");
//        producerService.send(msg);
//        var msg2 = new Message(messageIndex.get(), "Hola mundo" + messageIndex.getAndIncrement() + " !");
//        producerService.send2(msg2);
        var msg3 = new Message(messageIndex.get(), "Ciao mondo" + messageIndex.getAndIncrement() + " !");
        producerService.send3(msg3);
    }
}
