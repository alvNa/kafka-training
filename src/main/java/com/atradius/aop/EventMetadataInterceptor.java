package com.atradius.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.atradius.utils.MetadataUtils.printEventMetadataLogs;

@Component
@Slf4j
public class EventMetadataInterceptor<K,V> implements ProducerInterceptor<K, V> {

    @Override
    public void configure(Map<String, ?> map) {

    }

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        log.info("Headers {}", record.headers());
        printEventMetadataLogs(log, record.headers());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }
}