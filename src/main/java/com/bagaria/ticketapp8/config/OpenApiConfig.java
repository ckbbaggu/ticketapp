package com.bagaria.ticketapp8.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Ticket Management API")
                        .version("1.0")
                        .description("""
                                API documentation for the Ticket Management System.
                                
                                This API allows users to:
                                - Create tickets
                                - View tickets
                                - Update ticket status
                                - Manage users
                                
                                Authentication:
                                JWT Bearer token required for protected endpoints.
                                """)
                        .contact(new Contact()
                                .name("chetan")
                                .email("chetankumarbagaria@gmail.com")
                                .url("https://github.com/ckbbaggu")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
