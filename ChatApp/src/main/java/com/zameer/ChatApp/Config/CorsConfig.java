package com.zameer.ChatApp.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // All APIs
                        .allowedOrigins("http://localhost:3000") // Allow React app
                        .allowedMethods("*") // GET, POST, PUT, DELETE
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow cookies/auth headers
            }
        };
    }
}
