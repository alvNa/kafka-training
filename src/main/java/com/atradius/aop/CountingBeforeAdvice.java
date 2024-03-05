//package com.atradius.aop;
//
//import org.springframework.aop.MethodBeforeAdvice;
//
//import java.lang.reflect.Method;
//
//public class CountingBeforeAdvice implements MethodBeforeAdvice {
//
//    private int count;
//
//    public void before(Method m, Object[] args, Object target) throws Throwable {
//        ++count;
//    }
//
//    public int getCount() {
//        return count;
//    }
//}