package com.bagaria.ticketapp8.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    UserRole user_role;

    /*@OneToMany(mappedBy = "user")
    private List<Ticket> tickets;*/

    @OneToMany(mappedBy = "createdBy")
    private List<Ticket> createdTickets;

    @OneToMany(mappedBy = "assignedTo")
    private List<Ticket> assignedTickets;
}
