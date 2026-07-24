package com.bagaria.ticketapp8.dto;

import com.bagaria.ticketapp8.entity.TicketPriority;
import com.bagaria.ticketapp8.entity.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class
TicketCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title;

    String description;

    @NotNull
    TicketPriority priority;
}
