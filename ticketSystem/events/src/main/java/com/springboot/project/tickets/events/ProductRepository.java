package com.springboot.project.tickets.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
     
    public List<Product> findByEventId(int eventId);
}
