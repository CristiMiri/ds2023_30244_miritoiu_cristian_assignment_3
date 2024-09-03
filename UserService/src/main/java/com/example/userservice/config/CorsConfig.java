package com.example.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080") // Specify allowed origins
                .allowedHeaders("*") // Specify allowed headers
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allo     wed HTTP methods
                .allowCredentials(true); // Allow credentials (e.g., cookies)


    }
}
