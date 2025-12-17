package com.springboot.project.tickets.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class EventsConfiguration {
    
    @Bean
    public WebClient webClient(@Value("${tickets.events.url}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }
}
