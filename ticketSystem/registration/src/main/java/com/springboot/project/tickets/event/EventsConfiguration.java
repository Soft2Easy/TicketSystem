package com.springboot.project.tickets.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration
@ConfigurationProperties(prefix = "tickets.events.url")
public class EventsConfiguration {
    
    @Bean
    public WebClient webClient(@Value("${tickets.events.url}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }

    @Bean
    public EventsClient eventsClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();
        
        return factory.createClient(EventsClient.class);
    }
}
