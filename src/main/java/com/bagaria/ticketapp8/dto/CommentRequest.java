package com.bagaria.ticketapp8.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "Comment cannot be empty")
        @Size(max = 500, message = "Comment cannot exceed 500 characters")
        String comment) {
}
