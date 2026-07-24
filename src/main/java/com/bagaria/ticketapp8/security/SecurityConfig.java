package com.bagaria.ticketapp8.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthConverter jwtAuthConverter() {
        return new JwtAuthConverter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthConverter jwtAuthConverter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**","/swagger-ui/**",
                                "/v3/api-docs/**","/actuator/**").permitAll()
                        .requestMatchers("/api/v1/tickets/user").hasRole("ADMIN")
                        //.requestMatchers("/api/tickets/**").hasRole("USER")
                        .requestMatchers("/api/tickets/**").authenticated()
                        .anyRequest().authenticated())
                //.httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 ->
                        //oauth2.jwt(Customizer.withDefaults())
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                )
                .build();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("admin123")).roles("ADMIN").build();
        UserDetails agent = User.builder().username("agent").password(passwordEncoder().encode("agent123")).roles("AGENT").build();
        UserDetails user = User.builder().username("CKB").password(passwordEncoder().encode("user123")).roles("USER").build();
        return new InMemoryUserDetailsManager(admin, agent, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}