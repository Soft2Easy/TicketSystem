package com.springboot.project.tickets.event;

public record Venue(
    int id,
    String name,
    String street,
    String city,
    String country
) {
    
}
