package com.bagaria.ticketapp8.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(int id) {
        super("Ticket not found with id: " + id);
    }
}
