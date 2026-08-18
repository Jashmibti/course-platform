package com.learningplatform.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("reporting-service",
                        r -> r.path("/api/reports/**")
                                .uri("http://reporting-service:8086"))

                .route("user-service",
                        r -> r.path("/api/users/**")
                                .uri("http://user-service:8080"))

                .route("course-service",
                        r -> r.path("/api/v1/courses/**")
                                .uri("http://course-service:8082"))

                .route("enrollment-service",
                        r -> r.path("/api/v1/enrollments/**")
                                .uri("http://enrollment-service:8083"))

                .build();
    }
}