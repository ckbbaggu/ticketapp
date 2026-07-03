package com.bagaria.ticketapp8.repository;

import com.bagaria.ticketapp8.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
    Optional<User> findByEmail(String email);
}
