package com.bagaria.ticketapp8.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int tid;

    @Schema(
            description = "Short description of the problem",
            example = "Unable to login"
    )
    String title;
    //String description;

    @Enumerated(EnumType.STRING)
    TicketStatus status;


    @Schema(
            description = "Priority level",
            example = "HIGH"
    )
    @Enumerated(EnumType.STRING)
    TicketPriority priority;

    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    //List<String> comments;
    Set<String> tags;

    /*@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;*/

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Comment> comments;
}