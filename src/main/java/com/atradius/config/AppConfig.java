package com.atradius.config;

import com.atradius.aspect.SetEventMetadataAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SetEventMetadataAspect setEventMetadataAspect() {
        return new SetEventMetadataAspect();
    }
}
