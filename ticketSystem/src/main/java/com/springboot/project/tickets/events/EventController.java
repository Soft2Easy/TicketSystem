package com.springboot.project.tickets.events;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class EventController {

    private final OrganizerRepository organizerRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

    public EventController(OrganizerRepository organizerRepository,
                           EventRepository eventRepository,
                           ProductRepository productRepository) {
        this.organizerRepository = organizerRepository;
        this.eventRepository = eventRepository;
        this.productRepository = productRepository;
    }

    
    
    @GetMapping(path = "/organizers")
    public List<Organizer> getAllOrganizers() {
        return organizerRepository.findAll();
    }

    @GetMapping(path = "/events")
    public List<Event> getEventsByOrganizer(@RequestParam("organizerId")int organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    @GetMapping(path = "/events/{Id}")
    public Event getEventById(@PathVariable("Id") int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event with id " + eventId + " not found"));
    }
    
    @GetMapping(path = "/products")
    public List<Product> getProductsByEvent(@RequestParam("eventId") int eventId) {
        return productRepository.findByEventId(eventId);
    }
}
