package com.bagaria.ticketapp8.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;

    private List<String> message;
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
