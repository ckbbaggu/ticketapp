package com.bagaria.ticketapp8.exception;

import com.bagaria.ticketapp8.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFound(
            TicketNotFoundException ex
    ) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                404,
                "NOT_FOUND",
                ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidTicketStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatus(
            InvalidTicketStatusException ex
    ) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                400,
                "BAD_REQUEST",
                ex.getMessage(),
                null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                400,
                "VALIDATION_ERROR",
                "Validation failed",
                errors);
        return ResponseEntity.badRequest().body(response);
    }
}