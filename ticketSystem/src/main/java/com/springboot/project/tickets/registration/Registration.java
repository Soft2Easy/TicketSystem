package com.springboot.project.tickets.registration;

public record Registration(Integer id,
    Integer productId,
    String ticketCode,
    String attendeeName
) {
    
}
