package com.atradius.aspect;

import com.atradius.annotation.SetEventMetadata;
import com.atradius.model.Metadata;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.atradius.utils.EventMetadataConverter.toEventMetadata;

@Aspect
@Component
public class SetEventMetadataAspect {
    private static ThreadLocal<Metadata> metadataThreadLocal = new ThreadLocal<>();

    @Around("@annotation(com.atradius.annotation.SetEventMetadata)")
    private Object getMetadataFromAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = ((MethodInvocationProceedingJoinPoint) joinPoint).getArgs();
        var paramTypes = Arrays.stream(args).map(Object::getClass)
                .toArray(Class<?>[]::new);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> declaredClass = signature.getDeclaringType();
        Method method = declaredClass.getDeclaredMethod(signature.getName(), paramTypes);
        metadataThreadLocal.set(toEventMetadata(method.getAnnotation(SetEventMetadata.class)));
        return joinPoint.proceed();
    }

    public static Metadata getEventMetadata(){
        var metadata = metadataThreadLocal.get();
        metadataThreadLocal.remove();
        return metadata;
    }
}
