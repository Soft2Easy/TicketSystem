package com.springboot.project.tickets.events;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class EventController {

    private final OrganizerRepository organizerRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;
    private final VenueRepository venueRepository;

    public EventController(OrganizerRepository organizerRepository,
                           EventRepository eventRepository,
                           ProductRepository productRepository,
                           VenueRepository venueRepository) {
        this.organizerRepository = organizerRepository;
        this.eventRepository = eventRepository;
        this.productRepository = productRepository;
        this.venueRepository = venueRepository;
    }

    @PostMapping(path = "/event/create")
public Event createEvent(@RequestBody Event event) {
    // Fetch Organizer by ID
    Organizer organizer = organizerRepository.findById(event.getOrganizer().getId())
            .orElseThrow(() -> new NoSuchElementException("Organizer with id " + event.getOrganizer().getId() + " not found"));

    // Fetch Venue by ID
    Venue venue = venueRepository.findById(event.getVenue().getId())
            .orElseThrow(() -> new NoSuchElementException("Venue with id " + event.getVenue().getId() + " not found"));

    // Create and save Event with the fetched entities
    return eventRepository.save(new Event((Integer) null, event.getName(), event.getStartDate(), event.getEndDate(), organizer, venue));
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
