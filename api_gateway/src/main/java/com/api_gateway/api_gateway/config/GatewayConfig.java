package com.api_gateway.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the API Gateway.
 */
@Configuration
public class GatewayConfig {

        /**
        * Configures the routes for the API Gateway, mapping incoming paths to the appropriate microservices.
        *
        * @param builder the route locator builder
        * @return the configured route locator
        */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service", r -> r
                .path("/auth/**")
                .uri("lb://AUTH-SERVICE"))
            
            .route("event-service", r -> r
                .path("/events/**","/venues/**")
                .uri("lb://EVENT-SERVICE"))
            
            .route("ticket-service", r -> r
                .path("/tickets/**")
                .uri("lb://TICKET-SERVICE"))
            
            .route("payment-service", r -> r
                .path("/payments/**")
                .uri("lb://PAYMENT-SERVICE"))
            .build();
    }
}