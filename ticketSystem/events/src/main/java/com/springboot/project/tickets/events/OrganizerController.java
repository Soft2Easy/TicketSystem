package com.springboot.project.tickets.events;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {
    private final OrganizerRepository organizerRepository;

    public OrganizerController(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    @PostMapping("/create")
    public Organizer createOrganizer(@RequestBody Organizer organizer) {
        return organizerRepository.save(organizer);
    }
}