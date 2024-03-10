package com.atradius.aop;

import com.atradius.handler.annotation.SetEventMetadata;
import com.atradius.model.Metadata;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static com.atradius.utils.MetadataUtils.addCustomMetadata;
import static com.atradius.utils.MetadataUtils.printEventMetadataLogs;

@Aspect
@Component
@Slf4j
public class EventMetadataInterceptor<K,V> implements ProducerInterceptor<K, V> {

    private static ThreadLocal<Metadata> metadataThreadLocal = new ThreadLocal<>();

    @Override
    public void configure(Map<String, ?> map) {

    }

    @Around("@annotation(com.atradius.handler.annotation.SetEventMetadata)")
    private void getMetadataFromAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = ((MethodInvocationProceedingJoinPoint) joinPoint).getArgs();
        var paramTypes = Arrays.stream(args).map(Object::getClass)
                .toArray(Class<?>[]::new);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> declaredClass = signature.getDeclaringType();
        Method method = declaredClass.getDeclaredMethod(signature.getName(), paramTypes);
        metadataThreadLocal.set(getMetadataFromAnnotation(
                                method.getAnnotation(SetEventMetadata.class)));
        joinPoint.proceed();
    }

    @SneakyThrows
    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        log.info("Headers {}", record.headers());
        log.info("Before");
        printEventMetadataLogs(log, record.headers());
        addCustomMetadata(record, metadataThreadLocal.get());
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

    private static Metadata getMetadataFromAnnotation(SetEventMetadata annotation) {
        return Metadata.builder()
                .eventName(annotation.eventName())
                .eventDomain(annotation.domain())
                .eventSubdomain(annotation.subdomain())
                .build();
    }
}