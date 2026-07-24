package com.bagaria.ticketapp8.service;

import com.bagaria.ticketapp8.dto.TicketCreateRequest;
import com.bagaria.ticketapp8.dto.TicketRequest;
import com.bagaria.ticketapp8.dto.TicketResponse;
import com.bagaria.ticketapp8.entity.*;
import com.bagaria.ticketapp8.exception.InvalidTicketStatusException;
import com.bagaria.ticketapp8.exception.TicketNotFoundException;
import com.bagaria.ticketapp8.exception.UnauthorizedActionException;
import com.bagaria.ticketapp8.mapper.TicketMapper;
import com.bagaria.ticketapp8.repository.TicketRepository;
import com.bagaria.ticketapp8.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TicketMapper mapper;

    @Autowired
    UserRepository userRepository;



    private TicketStatus parseStatus(String status) {
        try {
            return TicketStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTicketStatusException(status);
        }
    }

    //delete a ticket
    @Transactional
    public String removeTicket(int tid) {
        ticketRepository.deleteById(tid);
        return "Ticket removed";
    }

    //update a ticket
    @Transactional
    public TicketResponse updateTicket(TicketRequest ticketRequest, Authentication authentication) {
        Ticket ticket = ticketRepository.findById(ticketRequest.getTid()).orElseThrow(() -> new TicketNotFoundException(ticketRequest.getTid()));

        ticket.setTitle(ticketRequest.getTitle());
        ticket.setDescription(ticketRequest.getDescription());
        ticket.setPriority(ticketRequest.getPriority());

        if (ticketRequest.getStatus() != null) {
            updateStatus(ticketRequest.getTid(), ticketRequest.getStatus().toString(), authentication);
        }

        ticketRepository.save(ticket);
        return mapper.toResponse(ticket);
    }

    //assign a ticket
    //Only AGENT or ADMIN can assign tickets
    @Transactional
    public TicketResponse assignTicket(int tid, int assignee_id) {
        Ticket ticket = ticketRepository.findById(tid).orElseThrow(() -> new TicketNotFoundException(tid));
        User assignee = userRepository.findById(assignee_id).orElseThrow();

        //if (assignee.getUser_role() != UserRole.ADMIN && assignee.getUser_role() != UserRole.AGENT) {
            //throw new UnauthorizedActionException("Only ADMIN or AGENT can assign");
        if (ticket.getStatus().equals(TicketStatus.CLOSED)){
            throw new UnauthorizedActionException("You cannot update closed ticket");
        }

        ticket.setAssignedTo(assignee);
        return mapper.toResponse(ticket);
    }

    //update ticket status
    //open a closed ticket. cannot be opened
    //close a ticket. user or admin can close the ticket
    @Transactional
    public TicketResponse updateStatus(int tid, String newStatus, Authentication authentication) {

        Ticket ticket = ticketRepository.findById(tid).orElseThrow(() -> new TicketNotFoundException(tid));

        String email = authentication.getName();

        if (ticket.getStatus() == TicketStatus.CLOSED && parseStatus(newStatus) != TicketStatus.CLOSED) {
            throw new InvalidTicketStatusException("Cannot reopen closed ticket");
        } else if (ticket.getStatus() == TicketStatus.CLOSED && parseStatus(newStatus) == TicketStatus.CLOSED) {
            throw new InvalidTicketStatusException("Ticket is already closed");
        } else if (ticket.getStatus() != TicketStatus.CLOSED && parseStatus(newStatus) == TicketStatus.CLOSED) {
            if (!(ticket.getCreatedBy().getEmail()).equals(email)) {
                if (!authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    throw new UnauthorizedActionException("You cannot close someone else's ticket");
                }
            }
        }

        ticket.setStatus(parseStatus(newStatus.toString()));
        return mapper.toResponse(ticket);
    }

    //get tickets
    public Page<TicketResponse> searchTickets(TicketStatus status,
                                              TicketPriority priority,
                                              Integer tid,
                                              Pageable pageable) {
        return ticketRepository.searchTickets(status,
                priority,
                tid,
                pageable).map(mapper::toResponse);
    }

    //add a new ticket
    public TicketResponse createTicket(TicketCreateRequest ticketRequest, Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UnauthorizedActionException(authentication.getName()));

        Ticket ticket = new Ticket();
        ticket.setTitle(ticketRequest.getTitle());
        ticket.setDescription(ticketRequest.getDescription());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(ticketRequest.getPriority());
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());
        ticket.setCreatedBy(user);
        ticket.setAssignedTo(user);

        return mapper.toResponse(ticketRepository.save(ticket));
    }
}