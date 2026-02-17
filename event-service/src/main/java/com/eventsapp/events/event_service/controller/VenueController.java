package com.eventsapp.events.event_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.eventsapp.events.event_service.model.Venue;
import com.eventsapp.events.event_service.service.VenueService;



@RestController
@RequestMapping("/venues")
public class VenueController {
    
    private final VenueService venueService;
    
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping("/create")
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        Venue createdVenue = venueService.createVenue(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVenue);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Venue>> getAllVenues() {
        Iterable<Venue> venues = venueService.getAllVenues();
        return ResponseEntity.ok(venues);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        Venue venue = venueService.getVenueById(id);
        return ResponseEntity.ok(venue);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @RequestBody Venue venue) {
        Venue updatedVenue = venueService.updateVenue(id, venue);
        return ResponseEntity.ok(updatedVenue);
    }
    
}
