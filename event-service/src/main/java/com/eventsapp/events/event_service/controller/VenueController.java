package com.eventsapp.events.event_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.eventsapp.events.event_service.errorHandling.ResourceNotFoundException;

import com.eventsapp.events.event_service.model.Venue;
import com.eventsapp.events.event_service.repository.VenueRepository;



@RestController
@RequestMapping("/venues")
public class VenueController {
    
    private final VenueRepository venueRepository;
    
    public VenueController(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @PostMapping("/create")
    public Venue createVenue(@RequestBody Venue venue) {
        return venueRepository.save(venue);
    }

    @GetMapping("/all")
    public Iterable<Venue> getAllVenues() {
        return venueRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Venue getVenueById(@PathVariable Long id) {
        return venueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteVenue(@PathVariable Long id) {
        venueRepository.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public Venue updateVenue(@PathVariable Long id, @RequestBody Venue venue) {
        Venue existingVenue = venueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        existingVenue.setName(venue.getName());
        existingVenue.setStreet(venue.getStreet());
        existingVenue.setCity(venue.getCity());
        existingVenue.setCountry(venue.getCountry());
        return venueRepository.save(existingVenue);
    }
    
}
