package com.ticketservice.ticket.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "${event.service.url}")
public interface EventClient {
    
    @GetMapping("/events/exists/{id}")
    ResponseEntity<Void> checkEvent(@PathVariable("id") Long id);
    
}
