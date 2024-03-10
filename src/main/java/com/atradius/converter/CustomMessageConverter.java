//package com.atradius.converter;
//
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.header.Header;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.kafka.support.converter.RecordMessageConverter;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageHeaders;
//
//import java.lang.reflect.Type;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//public class CustomMessageConverter implements RecordMessageConverter {
//
//    @Override
//    public Message<?> toMessage(ConsumerRecord<?, ?> consumerRecord, Acknowledgment acknowledgment, Consumer<?, ?> consumer, Type type) {
//        System.out.println("Converting toMessage");
//        return new Message<>() {
//            @Override
//            public Object getPayload() {
//                return consumerRecord.value().toString();
//            }
//
//            @Override
//            public MessageHeaders getHeaders() {
//                var messageHeaders = new MessageHeaders(Arrays.stream(consumerRecord.headers().toArray())
//                        .collect(Collectors.toMap(Header::key, Header::value)));
//                return messageHeaders;
//            }
//        };
//    }
//
//    @Override
//    public ProducerRecord<?, ?> fromMessage(Message<?> message, String topic) {
//        System.out.println("Converting fromMessage");
//        return new ProducerRecord(topic, message.getPayload());
//    }
//}
