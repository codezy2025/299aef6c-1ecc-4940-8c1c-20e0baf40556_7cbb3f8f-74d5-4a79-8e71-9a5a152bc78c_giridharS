package com.java.coreTemplate.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(SessionAuthService sessionAuthService) {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(sessionAuthService));
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
