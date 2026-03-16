package com.eventsapp.events.event_service.service;


import org.springframework.stereotype.Service;

import com.eventsapp.events.event_service.errorHandling.ResourceNotFoundException;
import com.eventsapp.events.event_service.model.Venue;
import com.eventsapp.events.event_service.repository.VenueRepository;



@Service
public class VenueService {
    
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public Venue createVenue(Venue venue) {
         return venueRepository.save(venue);
    }

    public Iterable<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenueById(Long id) {
        return venueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
    }
        
    public Venue updateVenue(Long id, Venue venue) {
        Venue existingVenue = venueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        existingVenue.setName(venue.getName());
        existingVenue.setStreet(venue.getStreet());
        existingVenue.setCity(venue.getCity());
        existingVenue.setCountry(venue.getCountry());
        return venueRepository.save(existingVenue);
    }
}
