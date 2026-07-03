package com.bagaria.ticketapp8.exception;

public class InvalidTicketStatusException extends RuntimeException {

    public InvalidTicketStatusException(String status) {
        super("Invalid ticket status: " + status);
    }
}