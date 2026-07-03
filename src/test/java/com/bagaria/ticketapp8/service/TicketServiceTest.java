package com.bagaria.ticketapp8.service;

import com.bagaria.ticketapp8.dto.TicketRequest;
import com.bagaria.ticketapp8.dto.TicketResponse;
import com.bagaria.ticketapp8.entity.Ticket;
import com.bagaria.ticketapp8.entity.TicketPriority;
import com.bagaria.ticketapp8.entity.TicketStatus;
import com.bagaria.ticketapp8.entity.User;
import com.bagaria.ticketapp8.mapper.TicketMapper;
import com.bagaria.ticketapp8.repository.TicketRepository;
import com.bagaria.ticketapp8.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TicketService ticketService;

    @Captor
    private ArgumentCaptor<Ticket> ticketCaptor;

    private TicketRequest request;
    private User user;
    private Ticket ticket;
    private TicketResponse response;

    @BeforeEach
    void setUp() {
        request = new TicketRequest();
        request.setTitle("Unable to login");

        user = new User();
        user.setUid(1000);
        user.setEmail("john@gmail.com");

        ticket = new Ticket();
        ticket.setTid(1000);
        ticket.setTitle(request.getTitle());
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(user);

        response = new TicketResponse(ticket.getTid(), ticket.getTitle(), ticket.getStatus(), ticket.getPriority());
    }

    @Test
    void shouldCreateTicketSuccessfully() {

        // Arrange
        when(authentication.getName()).thenReturn("john@gmail.com");

        when(userRepository.findByEmail("john@gmail.com")).thenReturn(Optional.of(user));

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        when(ticketMapper.toResponse(ticket)).thenReturn(response);

        // Act
        TicketResponse result = ticketService.createTicket(request, authentication);

        // Assert Result
        assertNotNull(result);
        assertEquals(1000, result.tid());
        assertEquals("Unable to login", result.title());
        assertEquals(TicketStatus.OPEN, result.status());

        // Verify Interaction
        verify(userRepository).findByEmail("john@gmail.com");
        verify(ticketRepository).save(ticketCaptor.capture());
        verify(ticketMapper).toResponse(ticket);

        // Verify Saved Ticket
        Ticket savedTicket = ticketCaptor.getValue();

        assertEquals("Unable to login", savedTicket.getTitle());
        assertEquals(TicketPriority.LOW, savedTicket.getPriority());
        assertEquals(TicketStatus.OPEN, savedTicket.getStatus());
        assertEquals(user, savedTicket.getCreatedBy());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        //when(authentication.getName()).thenReturn("john@gmail.com");

        //when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ticketService.createTicket(request, authentication));

        assertEquals(null, exception.getMessage());

        verify(ticketRepository, never()).save(any());
    }

}