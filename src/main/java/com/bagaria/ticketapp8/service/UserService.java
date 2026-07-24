package com.bagaria.ticketapp8.service;

import com.bagaria.ticketapp8.dto.UserRequest;
import com.bagaria.ticketapp8.entity.User;
import com.bagaria.ticketapp8.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserRequest request, Authentication authentication) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUser_role(request.getUser_role());
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());

        return userRepository.save(user);
    }
}
