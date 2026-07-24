package com.bagaria.ticketapp8.repository;

import com.bagaria.ticketapp8.dto.TicketResponse;
import com.bagaria.ticketapp8.entity.Ticket;
import com.bagaria.ticketapp8.entity.TicketPriority;
import com.bagaria.ticketapp8.entity.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByPriority(TicketPriority priority);

    List<Ticket> findByStatusAndPriority(TicketStatus status, TicketPriority priority);

    List<Ticket> findByAssignedToIsNotNull();

    @Query("""
            SELECT t
            FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:priority IS NULL OR t.priority = :priority)
              AND (:tid IS NULL OR t.tid = :tid)
            """)
    Page<Ticket> searchTickets(TicketStatus status, TicketPriority priority, Integer tid, Pageable pageable);
}
