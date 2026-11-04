package com.easygest.springboot.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;

    public CorsConfig(
            @Value("${EASYGEST_CORS_ALLOWED_ORIGINS:https://easygesterp.vercel.app,https://*.vercel.app,http://localhost:*,http://127.0.0.1:*}") String origins) {
        List<String> list = new ArrayList<>(Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList());

        // Garante que localhost e 127.0.0.1 em qualquer porta estejam sempre liberados (npm run dev)
        if (!list.contains("http://localhost:*")) {
            list.add("http://localhost:*");
        }
        if (!list.contains("http://127.0.0.1:*")) {
            list.add("http://127.0.0.1:*");
        }

        this.allowedOrigins = list.toArray(String[]::new);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
