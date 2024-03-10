package com.atradius.handler.annotation.reactive;

import com.atradius.handler.annotation.EventMetadata;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.reactive.HeadersMethodArgumentResolver;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Map;

import static com.atradius.utils.MetadataUtils.getCustomMetadata;

public class EventMetadataMethodArgumentResolver extends HeadersMethodArgumentResolver {
    public EventMetadataMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return parameter.hasParameterAnnotation(EventMetadata.class) && Map.class.isAssignableFrom(paramType) || MessageHeaders.class == paramType || MessageHeaderAccessor.class.isAssignableFrom(paramType);
    }

    @Override
    public Object resolveArgumentValue(MethodParameter parameter, Message<?> message) {
        Map<String, Object> headers = (Map<String, Object>) super.resolveArgumentValue(parameter, message);
        var eventCustomMetadata = getCustomMetadata(headers);
        return eventCustomMetadata;
    }String eventName,
}
