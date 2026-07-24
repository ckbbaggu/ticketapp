package com.bagaria.ticketapp8.repository;

import com.bagaria.ticketapp8.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("""
    SELECT c.message
    FROM Comment c
    WHERE c.ticket.tid = :ticketId
    ORDER BY c.createdDate ASC
    """)
    List<String> findMessagesByTicketId(@Param("ticketId") Integer ticketId);
}
