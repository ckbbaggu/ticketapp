package com.bagaria.ticketapp8.dto;

import com.bagaria.ticketapp8.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank
    String name;
    @NotBlank
    String email;
    @NotBlank
    UserRole user_role;
}
