package com.springboot.project.tickets.events;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class OrganizerRepository {
    private final List<Organizer> organizers = List.of( 
                new Organizer(101, "Globomantics", "Globomantics Technologies Corporation"),
                new Organizer(102, "Carved Rock", "Carved Rock Sports Equipment"));

    public List<Organizer> findAll() {
        return organizers;
    }
    
}
