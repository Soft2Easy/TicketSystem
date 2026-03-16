package com.ticketservice.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor userJwtForwardingInterceptor() {

        return new RequestInterceptor() {

            @Override
            public void apply(RequestTemplate requestTemplate) {

                // Get the current HTTP request attributes
                ServletRequestAttributes requestAttributes
                        = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (requestAttributes == null) {
                    return; // No active HTTP request (e.g. async call)
                }

                // Extract the current HTTP request
                HttpServletRequest currentRequest = requestAttributes.getRequest();

                // Get the Authorization header from the incoming request
                String authorizationHeader = currentRequest.getHeader("Authorization");

                if (authorizationHeader == null || authorizationHeader.isBlank()) {
                    return; // No JWT present
                }

                // Forward the Authorization header to the outgoing Feign request
                requestTemplate.header("Authorization", authorizationHeader);
            }
        };
    }
}
