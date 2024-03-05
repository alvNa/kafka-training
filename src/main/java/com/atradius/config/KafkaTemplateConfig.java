//package com.atradius.config;
//
//import com.atradius.aop.KafkaTemplateInterceptor;
//import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//
//@Configuration
//@EnableAspectJAutoProxy
//public class KafkaTemplateConfig {
//
//  public static final String KAFKA_TEMPLATE_INTERCEPTOR = "kafkaTemplateInterceptor";
//
//  @Bean(name = KAFKA_TEMPLATE_INTERCEPTOR)
//  public KafkaTemplateInterceptor kafkaTemplateInterceptor() {
//    return new KafkaTemplateInterceptor();
//  }
//
////  @Bean
////  public BeanNameAutoProxyCreator interceptorAutoProxy() {
////    BeanNameAutoProxyCreator kafkaTemplateAutoProxy = new BeanNameAutoProxyCreator();
////    kafkaTemplateAutoProxy.setProxyTargetClass(true);
////    kafkaTemplateAutoProxy.setBeanNames("org.springframework.kafka.core.*KafkaTemplate.*(..)");
////    kafkaTemplateAutoProxy.setInterceptorNames(KAFKA_TEMPLATE_INTERCEPTOR);
////    return kafkaTemplateAutoProxy;
////  }
//
//  @Bean
//  public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
//    BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
//    beanNameAutoProxyCreator.setProxyTargetClass(true);
//    beanNameAutoProxyCreator.setBeanNames(
////            "*Controller",
////            "*Service",
////            "org.springframework.kafka.core.*KafkaTemplate.send*(..)*",
////            "*KafkaTemplate.*(..)*",
////            "*KafkaTemplate.send(..)"
//           // "*KafkaTemplate"
//    ); // If i put "*Service" instead, then i get an exception.
////    transactionAutoProxy.setBeanNames("acheng1314.cn.service.*ServiceImpl.*(..)");
//    beanNameAutoProxyCreator.setInterceptorNames(
////            "aopAfterMethod", "aopBeforeMethod", "aopThrowException", "aopAroundMethod",
//            KAFKA_TEMPLATE_INTERCEPTOR,
//            KafkaConfig.AVRO_KAFKA_TEMPLATE);
////    beanNameAutoProxyCreator.setAdvisorAdapterRegistry();
//    return beanNameAutoProxyCreator;
//  }
//
////  @Override
////  public void addInterceptors(InterceptorRegistry registry) {
////    registry.addInterceptor(kafkaTemplateInterceptor());
////  }
//
////  <bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"/>
//}
