package com.bagaria.ticketapp8.controller;

import com.bagaria.ticketapp8.dto.TicketRequest;
import com.bagaria.ticketapp8.dto.TicketResponse;
import com.bagaria.ticketapp8.entity.TicketPriority;
import com.bagaria.ticketapp8.entity.TicketStatus;
import com.bagaria.ticketapp8.security.SecurityConfig;
import com.bagaria.ticketapp8.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@Import(SecurityConfig.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private JwtDecoder jwtDecoder;
    /*
    @Test
    void shouldCreateTicketSuccessfully() throws Exception {

        // Arrange
        TicketRequest request = new TicketRequest();
        request.setTitle("Unable to login");

        TicketResponse response = new TicketResponse(1000,"Unable to login",TicketStatus.OPEN,TicketPriority.HIGH);

        when(ticketService.createTicket(any(TicketRequest.class), any(Authentication.class))).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .with(jwt().authorities(() -> "create:tickets"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tid").value(1000))
                .andExpect(jsonPath("$.title").value("Unable to login"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(ticketService).createTicket(any(TicketRequest.class), any(Authentication.class));
    }*/
}
