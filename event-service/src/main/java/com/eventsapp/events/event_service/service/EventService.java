package com.eventsapp.events.event_service.service;

import org.springframework.stereotype.Service;

import com.eventsapp.events.event_service.errorHandling.ResourceNotFoundException;
import com.eventsapp.events.event_service.model.Event;
import com.eventsapp.events.event_service.repository.EventRepository;
import com.eventsapp.events.event_service.repository.VenueRepository;

import jakarta.transaction.Transactional;

@Service
public class EventService {
    
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public EventService(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Transactional
    public Event createEvent(Event event) {

        if (event.getVenueId() == null) {
            throw new IllegalArgumentException("Venue ID is required");
        }

        if (!venueRepository.existsById(event.getVenueId())) {
            throw new ResourceNotFoundException("Venue not found with id: " + event.getVenueId());
        }
        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    public Iterable<Event> getAllEvents() {

        return eventRepository.findAll();
    }

    @Transactional
    public Event updateEvent(Long id, Event event) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        if (event.getVenueId() != null &&
            !venueRepository.existsById(event.getVenueId())) {
                    throw new ResourceNotFoundException("Venue not found with id: " + event.getVenueId());
        }
        existingEvent.setName(event.getName());
        existingEvent.setOrganizerId(event.getOrganizerId());
        existingEvent.setVenueId(event.getVenueId());
        existingEvent.setStartDate(event.getStartDate());
        existingEvent.setEndDate(event.getEndDate());
        return eventRepository.save(existingEvent);
    }

    public boolean eventExists(Long id) {
        return eventRepository.existsById(id);
    }

}
