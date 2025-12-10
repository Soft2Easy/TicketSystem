package com.springboot.project.tickets.events;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {
   
    public Optional<Venue> findById(int id);
}
