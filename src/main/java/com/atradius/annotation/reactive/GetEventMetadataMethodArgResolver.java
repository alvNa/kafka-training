//package com.atradius.annotation.reactive;
//
//import com.atradius.annotation.GetEventMetadata;
//import com.atradius.model.Metadata;
//import org.springframework.core.MethodParameter;
//import org.springframework.lang.Nullable;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.handler.invocation.reactive.SyncHandlerMethodArgumentResolver;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.reflect.Method;
//
//import static com.atradius.utils.EventMetadataConverter.toEventMetadata;
//
///**
// * You can see the reference class for this implementation in
// * org.springframework.messaging.handler.annotation.reactive.HeadersMethodArgumentResolver
// * */
//public class GetEventMetadataMethodArgResolver implements SyncHandlerMethodArgumentResolver{
//    public GetEventMetadataMethodArgResolver() {
//    }
//
//    public boolean supportsParameter(MethodParameter parameter) {
//        Class<?> paramType = parameter.getParameterType();
//        return parameter.hasParameterAnnotation(GetEventMetadata.class) && Metadata.class.isAssignableFrom(paramType);
//    }
//
//    @Nullable
//    public Object resolveArgumentValue(MethodParameter parameter, Message<?> message) {
//        Class<?> paramType = parameter.getParameterType();
//        if (Metadata.class.isAssignableFrom(paramType)) {
//            var headers = message.getHeaders();
//            return toEventMetadata(headers);
//        }
//        else {
//            MessageHeaderAccessor accessor;
//            if (MessageHeaderAccessor.class == paramType) {
//                accessor = MessageHeaderAccessor.getAccessor(message, MessageHeaderAccessor.class);
//                return accessor != null ? accessor : new MessageHeaderAccessor(message);
//            } else if (MessageHeaderAccessor.class.isAssignableFrom(paramType)) {
//                accessor = MessageHeaderAccessor.getAccessor(message, MessageHeaderAccessor.class);
//                if (accessor != null && paramType.isAssignableFrom(accessor.getClass())) {
//                    return accessor;
//                } else {
//                    Method method = ReflectionUtils.findMethod(paramType, "wrap", new Class[]{Message.class});
//                    if (method == null) {
//                        throw new IllegalStateException("Cannot create accessor of type " + paramType + " for message " + message);
//                    } else {
//                        return ReflectionUtils.invokeMethod(method, (Object)null, new Object[]{message});
//                    }
//                }
//            } else {
//                throw new IllegalStateException("Unexpected parameter of type " + paramType + " in method " + parameter.getMethod() + ". ");
//            }
//        }
//    }
//}
