package com.bagaria.ticketapp8.dto;

import com.bagaria.ticketapp8.entity.TicketPriority;
import com.bagaria.ticketapp8.entity.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record TicketResponse(int tid,
                             String title,
                             String description,
                             TicketStatus status,
                             TicketPriority priority//,
                             //LocalDateTime createdDate,
                             //LocalDateTime updatedDate,
                             //List<String> comments,
                             //Set<String> tags
) {}
