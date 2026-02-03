package com.eventsapp.events.event_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventsapp.events.event_service.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
}
