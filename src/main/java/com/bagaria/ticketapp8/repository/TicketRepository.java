package com.bagaria.ticketapp8.repository;

import com.bagaria.ticketapp8.entity.Ticket;
import com.bagaria.ticketapp8.entity.TicketPriority;
import com.bagaria.ticketapp8.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByPriority(TicketPriority priority);

    List<Ticket> findByStatusAndPriority(TicketStatus status, TicketPriority priority);

    List<Ticket> findByAssignedToIsNotNull();
}
