package com.springboot.project.tickets.registration;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document("registrations")
public record Registration(@Id String Id,
    @NotNull(message = "productId is required") Integer productId,
    String ticketCode,
    @NotBlank(message="attendeeName is required") String attendeeName
) {
    
}
