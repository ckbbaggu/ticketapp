package com.bagaria.ticketapp8.mapper;

import com.bagaria.ticketapp8.dto.TicketResponse;
import com.bagaria.ticketapp8.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    public TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getTid(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }
}
