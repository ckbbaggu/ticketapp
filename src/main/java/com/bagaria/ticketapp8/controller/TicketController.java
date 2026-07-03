package com.bagaria.ticketapp8.controller;

import com.bagaria.ticketapp8.dto.TicketRequest;
import com.bagaria.ticketapp8.dto.TicketResponse;
import com.bagaria.ticketapp8.entity.TicketStatus;
import com.bagaria.ticketapp8.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.OffsetScrollPositionHandlerMethodArgumentResolverSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:8000"
)
@RestController
@Tag(
        name = "Ticket Management",
        description = "Operations related to creating and managing support tickets"
)
public class TicketController {

    @Autowired
    TicketService ticketService;
    @Autowired
    private OffsetScrollPositionHandlerMethodArgumentResolverSupport offsetScrollPositionHandlerMethodArgumentResolverSupport;

    @GetMapping("/api/tickets")
    @Operation(
            summary = "Get tickets",
            description = "Retrieves tickets based on passed status, page, size, and tid"
    )
    public ResponseEntity<?> getTickets(@PathVariable(required = false) String status,
                                        //@PathVariable(required = false) String priority,
                                        @PathVariable(required = false) Integer page,
                                        @PathVariable(required = false) Integer size,
                                        @PathVariable(required = false) Integer tid) {
        if (status != null) {
            //return ResponseEntity.status(HttpStatus.OK).body(ticketService.getByStatus(status));
            return ResponseEntity.ok(ticketService.getByStatus(status));
        }
        /*if (priority != null) {
            return ticketService.getByPriority(priority);
        }*/
        if (page != null && size != null) {
            return ResponseEntity.ok(ticketService.getTickets(page, size));
        }
        if (tid != null) {
            return ResponseEntity.ok(ticketService.getTicketById(tid));
        }
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/tickets/{tid}")
    public ResponseEntity<String> removeTicket(@PathVariable int tid) {
        return ResponseEntity.ok(ticketService.removeTicket(tid));
    }

    @PreAuthorize("hasAuthority('create:tickets')")
    @PostMapping("/api/tickets")
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request, authentication));
    }

    @PreAuthorize("hasAuthority('update:tickets')")
    @PutMapping("/api/tickets")
    public ResponseEntity<TicketResponse> updateTicket(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ticketService.updateTicket(request));
    }

    //close a ticket
    @PatchMapping("/api/tickets/{tid}/status/toclose")
    public ResponseEntity<TicketResponse> closeTicket(@PathVariable int tid,
                                                      Authentication authentication) {
        return ResponseEntity.ok(ticketService.closeTicket(tid, authentication));
    }

    //assign a ticket
    @PreAuthorize("hasAuthority('assign:tickets')")
    @PatchMapping("/api/tickets/{tid}/assign/{assignee_id}")
    public ResponseEntity<TicketResponse> assignTicket(@PathVariable int tid,
                                                       @PathVariable int assignee_id) {
        return ResponseEntity.ok(ticketService.assignTicket(tid, assignee_id));
    }

    //open a closed ticket
    @PatchMapping("/api/tickets/{tid}/status/{newStatus}")
    public ResponseEntity<TicketResponse> updateStatus(@PathVariable int tid, @PathVariable TicketStatus newStatus) {
        return ResponseEntity.ok(ticketService.updateStatus(tid, newStatus));
    }

    @GetMapping("/api/public/hello")
    public String hello() {
        return "Public Endpoint";
    }
}
