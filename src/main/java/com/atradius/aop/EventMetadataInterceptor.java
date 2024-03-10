package com.atradius.aop;

import com.atradius.examples.Message;
import com.atradius.handler.annotation.SetEventMetadata;
import com.atradius.model.Metadata;
import com.atradius.services.ProducerService;
import com.atradius.utils.ActionTypesConstants;
import com.atradius.utils.DomainConstants;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.atradius.utils.EventNames.BUYER_PI_READY;
import static com.atradius.utils.MetadataUtils.addCustomMetadata;
import static com.atradius.utils.MetadataUtils.printEventMetadataLogs;

@Aspect
@Component
@Slf4j
public class EventMetadataInterceptor<K,V> implements ProducerInterceptor<K, V> {

    private static ThreadLocal<Metadata> metadataThreadLocal = new ThreadLocal<>();
    //private Metadata metadata;

    @Override
    public void configure(Map<String, ?> map) {

    }

    @Around("@annotation(com.atradius.handler.annotation.SetEventMetadata)")
    private void getMetadata(ProceedingJoinPoint joinPoint) throws Throwable {
        //long startTime = System.currentTimeMillis();

        //Object result = joinPoint.proceed();
//
//        String className = joinPoint.getSignature().getDeclaringType().getName();
        //String methodName = joinPoint.getSignature().getName();
        //Class methodType = joinPoint.getSignature().getDeclaringType();
        //MethodInvocationProceedingJoinPoint src = joinPoint.getSourceLocation();
        var args = ((MethodInvocationProceedingJoinPoint) joinPoint).getArgs();
        var paramTypes = Arrays.stream(args).map(Object::getClass)
                .toArray(Class<?>[]::new);
        //var mi = ((MethodInvocationProceedingJoinPoint) joinPoint).getThis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> declaredClass = signature.getDeclaringType();
        //Method method = declaredClass.getMethod(signature.getName(), Message.class);
        //Method method = declaredClass.getMethod(signature.getName(), paramTypes);
        Method method = declaredClass.getDeclaredMethod(signature.getName(), paramTypes);
        //SetEventMetadata annotation = declaredClass.getAnnotation(SetEventMetadata.class);
        //SetEventMetadata annotation = method.getAnnotation(SetEventMetadata.class);
        //var metadata = getMetadataFromAnnotation(annotation);
        metadataThreadLocal.set(getMetadataFromAnnotation(
                                method.getAnnotation(SetEventMetadata.class)));
        //long endTime = System.currentTimeMillis();
        //long executionTime = endTime - startTime;

//        System.out.println(String.format("Method %s execution time: %d ms",
//                        joinPoint.getSignature(), 0));

        //return result;
        joinPoint.proceed();
    }

    @SneakyThrows
    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
//        Class<ProducerService> clazz = ProducerService.class;
//        Method method = clazz.getMethod("send",Message.class);

        // Check if the annotation is present
//        if (method.isAnnotationPresent(SetEventMetadata.class)) {
//            // Get the annotation instance
//            SetEventMetadata annotation = method.getAnnotation(SetEventMetadata.class);
//
//            // Access annotation elements
//            var eventName = annotation.eventName();
//            var domain = annotation.domain();
//
//            log.info("Annotation eventName: {}", eventName);
//            log.info("Annotation domain: {}", domain);
//        }

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
        //metadata = null;
        metadataThreadLocal.remove();
    }

    private static Metadata getMetadataFromAnnotation(SetEventMetadata annotation) {
        return Metadata.builder()
                .eventName(annotation.eventName())
                .eventDomain(annotation.domain())
                .eventSubdomain(annotation.subdomain())
                .build();
    }

//    private static Metadata getMetadata() {
//        return Metadata.builder()
//                .eventName(BUYER_PI_READY)
//                .eventDomain(DomainConstants.CREDIT_INSURANCE)
//                .eventType(ActionTypesConstants.NOTIFICATION)
//                .correlationId(Optional.of(UUID.randomUUID()))
//                .build();
//    }

}