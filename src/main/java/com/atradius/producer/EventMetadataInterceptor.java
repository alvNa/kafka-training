package com.atradius.producer;

import com.atradius.annotation.SetEventMetadata;
import com.atradius.model.Metadata;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static com.atradius.aspect.SetEventMetadataAspect.getEventMetadata;
import static com.atradius.utils.MetadataUtils.addEventMetadata;
import static com.atradius.utils.MetadataUtils.printEventMetadataLogs;

@Component
@Slf4j
public class EventMetadataInterceptor<K,V> implements ProducerInterceptor<K, V> {

    @Override
    public void configure(Map<String, ?> map) {

    }

    @SneakyThrows
    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        log.info("Headers {}", record.headers());
        log.info("Before");
        printEventMetadataLogs(log, record.headers());
        addEventMetadata(record.headers(), getEventMetadata());
        log.info("After");
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