package com.atradius.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class KafkaTemplateInterceptor implements MethodInterceptor {

  public Object invoke(MethodInvocation invocation) throws Throwable {
    System.out.println("Before: invocation=[" + invocation + "]");
    Object rval = invocation.proceed();
    System.out.println("Invocation returned");
    return rval;
  }
}
