package com.springboot.project.tickets.event;

import java.time.LocalDate;

public record Event(
    int id,
    String name,
    Organizer organizer,
    Venue venue,
    LocalDate starDate,
    LocalDate endDate
) {
    
}
