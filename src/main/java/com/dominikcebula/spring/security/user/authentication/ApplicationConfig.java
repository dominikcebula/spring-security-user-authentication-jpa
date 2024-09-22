package com.dominikcebula.spring.security.user.authentication;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
