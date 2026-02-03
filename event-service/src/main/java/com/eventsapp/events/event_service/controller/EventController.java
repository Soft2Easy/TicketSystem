package com.eventsapp.events.event_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eventsapp.events.event_service.repository.EventRepository;
import com.eventsapp.events.event_service.model.Event;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.eventsapp.events.event_service.errorHandling.ResourceNotFoundException;


@RestController
@RequestMapping("/events")
public class EventController {
    
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping("/create")
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @GetMapping("/all")
    public Iterable<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        existingEvent.setName(event.getName());
        existingEvent.setOrganizerId(event.getOrganizerId());
        existingEvent.setVenueId(event.getVenueId());
        existingEvent.setStartDate(event.getStartDate());
        existingEvent.setEndDate(event.getEndDate());
        return eventRepository.save(existingEvent); 
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventRepository.deleteById(id);
    }
}
