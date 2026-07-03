package com.bagaria.ticketapp8.service;

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

    //get all tickets
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    //get paginated tickets
    public Page<TicketResponse> getTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return ticketRepository.findAll(pageable).map(mapper::toResponse);
    }

    //get one specific ticket
    public TicketResponse getTicketById(int id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        return mapper.toResponse(ticket);
    }

    //add a new ticket
    public TicketResponse createTicket(TicketRequest ticketRequest, Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UnauthorizedActionException(authentication.getName()));

        Ticket ticket = new Ticket();
        ticket.setTitle(ticketRequest.getTitle());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.LOW);
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setUpdatedDate(LocalDateTime.now());
        ticket.setTags(Set.of("new"));
        //ticket.setUser(user);
        ticket.setCreatedBy(user);

        return mapper.toResponse(ticketRepository.save(ticket));
    }

    //update a ticket
    public TicketResponse updateTicket(TicketRequest ticketRequest) {
        Ticket ticket = ticketRepository.findById(ticketRequest.getTid()).orElseThrow(() -> new TicketNotFoundException(ticketRequest.getTid()));

        ticket.setTitle(ticketRequest.getTitle());
        ticketRepository.save(ticket);
        return mapper.toResponse(ticket);
    }

    private TicketStatus parseStatus(String status) {
        try {
            return TicketStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTicketStatusException(status);
        }
    }

    public List<TicketResponse> getByStatus(String status) {
        return ticketRepository
                .findByStatus(parseStatus(status))
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public TicketResponse closeTicket(int tid, Authentication authentication) {

        String email = authentication.getName();

        System.out.println("Email1: " + email);
        authentication.getAuthorities().forEach(System.out::println);
        System.out.println("Email2: " + email);

        Ticket ticket = ticketRepository.findById(tid).orElseThrow(() -> new TicketNotFoundException(tid));

        if (!(ticket.getCreatedBy().getEmail()).equals(userRepository.findByEmail(email))) {
            if (!authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                throw new UnauthorizedActionException("You cannot close someone else's ticket");
            }
        }
        ticket.setStatus(TicketStatus.CLOSED);
        return mapper.toResponse(ticket);
    }

    //delete a ticket
    @Transactional
    public String removeTicket(int tid) {
        ticketRepository.deleteById(tid);
        return "Ticket removed";
    }

    //assign a ticket
    @Transactional
    public TicketResponse assignTicket(int tid, int assignee_id) {

        /*User currentUser = userRepository.findById(current_user_id).orElseThrow();

        if (currentUser.getUser_role() != UserRole.ADMIN && currentUser.getUser_role() != UserRole.AGENT) {
            throw new UnauthorizedActionException("Only ADMIN or AGENT can assign");
        }*/

        Ticket ticket = ticketRepository.findById(tid).orElseThrow(() -> new TicketNotFoundException(tid));
        User assignee = userRepository.findById(assignee_id).orElseThrow();
        ticket.setAssignedTo(assignee);
        return mapper.toResponse(ticket);
    }

    //open a closed ticket
    @Transactional
    public TicketResponse updateStatus(int tid, TicketStatus newStatus) {

        Ticket ticket = ticketRepository.findById(tid).orElseThrow(() -> new TicketNotFoundException(tid));

        if (ticket.getStatus() == TicketStatus.CLOSED && newStatus == TicketStatus.OPEN) {
            throw new InvalidTicketStatusException("Cannot reopen closed ticket");
        }
        ticket.setStatus(newStatus);
        return mapper.toResponse(ticket);
    }
}