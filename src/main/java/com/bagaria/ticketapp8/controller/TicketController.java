package com.bagaria.ticketapp8.controller;

import com.bagaria.ticketapp8.dto.*;
import com.bagaria.ticketapp8.entity.Comment;
import com.bagaria.ticketapp8.entity.TicketPriority;
import com.bagaria.ticketapp8.entity.TicketStatus;
import com.bagaria.ticketapp8.entity.User;
import com.bagaria.ticketapp8.service.CommentService;
import com.bagaria.ticketapp8.service.TicketService;
import com.bagaria.ticketapp8.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = "http://localhost:8000"
)
@RestController
@RequestMapping("/api/v1/tickets")
@Tag(
        name = "Ticket Management",
        description = "Operations related to creating and managing support tickets"
)
public class TicketController {

    @Autowired
    TicketService ticketService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @GetMapping("/{ticketId}/comments")
    public ResponseEntity<List<String>> getComments(@PathVariable Integer ticketId) {
        List<String> comments = commentService.getComments(ticketId);

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{ticketId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public String addComment(
            @PathVariable Integer ticketId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {

        return commentService.addComment(ticketId, request, authentication);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{tid}")
    public ResponseEntity<String> removeTicket(@PathVariable int tid) {
        return ResponseEntity.ok(ticketService.removeTicket(tid));
    }

    @PreAuthorize("hasAuthority('update:tickets')")
    @PutMapping()
    public ResponseEntity<TicketResponse> updateTicket(@Valid @RequestBody TicketRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ticketService.updateTicket(request,authentication));
    }

    //assign a ticket
    //Only AGENT or ADMIN can assign tickets
    @PreAuthorize("hasAuthority('assign:tickets')")
    @PatchMapping("/{tid}/assign/{assignee_id}")
    public ResponseEntity<TicketResponse> assignTicket(@PathVariable int tid,
                                                       @PathVariable int assignee_id) {
        return ResponseEntity.ok(ticketService.assignTicket(tid, assignee_id));
    }

    //update ticket status
    //open a closed ticket. cannot be opened
    //close a ticket. user or admin can close the ticket
    @PatchMapping("/{tid}/status/{newStatus}")
    public ResponseEntity<TicketResponse> updateStatus(@PathVariable int tid,
                                                       @PathVariable String newStatus,
                                                       Authentication authentication) {
        return ResponseEntity.ok(ticketService.updateStatus(tid, newStatus, authentication));
    }

    //get tickets
    @GetMapping()
    @Operation(
            summary = "Get tickets",
            description = "Retrieves tickets based on passed status, page, size, and tid"
    )
    public Page<TicketResponse> searchTickets(@RequestParam(required = false) TicketStatus status,
                                           @RequestParam(required = false) TicketPriority priority,
                                           @RequestParam(required = false) Integer id,
                                           Pageable pageable) {
        return ticketService.searchTickets(
                status,
                priority,
                id,
                pageable);
    }

    //create a ticket
    @PreAuthorize("hasAuthority('create:tickets')")
    @PostMapping()
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketCreateRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request, authentication));
    }

    //create a user
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('manage:users')")
    public ResponseEntity<User> createUser(@RequestBody UserRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request, authentication));
    }

    @GetMapping("/api/public/hello")
    public String hello() {
        return "Public Endpoint";
    }
}
