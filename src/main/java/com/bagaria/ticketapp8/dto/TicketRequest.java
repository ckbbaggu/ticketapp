package com.bagaria.ticketapp8.dto;

import com.bagaria.ticketapp8.entity.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class
TicketRequest {
    int tid;
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title;
    //String description;
    TicketStatus status;
    //TicketPriority priority;
    //LocalDateTime createdDate;
    //LocalDateTime updatedDate;
    //List<String> comments;
    //Set<String> tags;
}
