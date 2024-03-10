//package com.atradius.handler.annotation.reactive;
//
//import com.atradius.handler.annotation.SetEventMetadata;
//import org.springframework.core.MethodParameter;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.handler.invocation.reactive.SyncHandlerMethodArgumentResolver;
//
//import java.util.Map;
//
//public class SetEventMetadataMethodArgResolver implements SyncHandlerMethodArgumentResolver {
//    @Override
//    public Object resolveArgumentValue(MethodParameter parameter, Message<?> message) {
//        Class<?> paramType = parameter.getParameterType();
//        //Map<String, Object> headers = (Map<String, Object>) super.resolveArgumentValue(parameter, message);
//        return "resultado";
//    }
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        Class<?> paramType = parameter.getParameterType();
//        return paramType.isAnnotationPresent(SetEventMetadata.class);
//    }
//}
