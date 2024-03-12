package com.atradius.annotation.reactive;

import com.atradius.annotation.GetEventMetadata;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.reactive.HeadersMethodArgumentResolver;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Map;

import static com.atradius.utils.EventMetadataConverter.toEventMetadata;

public class GetEventMetadataMethodArgResolver2 extends HeadersMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return parameter.hasParameterAnnotation(GetEventMetadata.class) &&
                Map.class.isAssignableFrom(paramType) ||
                MessageHeaders.class == paramType ||
                MessageHeaderAccessor.class.isAssignableFrom(paramType);
    }

    @Override
    public Object resolveArgumentValue(MethodParameter parameter, Message<?> message) {
        var obj = super.resolveArgumentValue(parameter, message);
        if (obj instanceof MessageHeaders){
            return toEventMetadata((MessageHeaders)obj);
        } else {
          return obj;
        }
    }
}
