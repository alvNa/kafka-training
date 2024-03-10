package com.atradius.handler.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetEventMetadata {
    String eventName();
    String domain() default "";
    String subdomain() default "";
}
