package com.eventsapp.events.event_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventsapp.events.event_service.model.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    
}
