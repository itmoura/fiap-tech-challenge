package com.fiap.itmoura.tech_challenge.service;

import com.fiap.itmoura.tech_challenge.exception.BadRequestException;
import com.fiap.itmoura.tech_challenge.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.model.entity.Users;
import com.fiap.itmoura.tech_challenge.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Transactional
    public UUID createUser(UserDTO userDTO) {
        log.info("Creating user: {}", userDTO);

        validateUser(userDTO);

        var user = userDTO.toEntity();
        user.setPassword(encoder.encode(userDTO.password()));
        user.setIsActive(true);

        log.info("User created successfully: {}", userDTO.email());

        return userRepository.save(user).getId();
    }

    public List<UserDTO> getAllUsers() {
        log.info("Retrieving all users");

        var users = userRepository.findAll();
        if (users.isEmpty()) {
            log.warn("No users found");
            return List.of();
        }

        log.info("Found {} users", users.size());
        return users.stream().map(UserDTO::fromEntity).toList();
    }

    public UserDTO getUserById(UUID userId) {
        log.info("Retrieving user by ID: {}", userId);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found with ID: " + userId));

        log.info("User found: {}", user.getEmail());
        return UserDTO.fromEntity(user);
    }

    private Boolean validateUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            log.error("User with email already exists: {}", userDTO.email());
            throw new ConflictRequestException("User with email already exists: " + userDTO.email());
        }

        if (userRepository.existsByPhone(userDTO.phone())) {
            log.error("User with phone already exists: {}", userDTO.phone());
            throw new ConflictRequestException("User with phone already exists: " + userDTO.phone());
        }

        return true;
    }

    public UserDTO updateUser(UserDTO userDTO) {
        getUserLogged();

//        var user = userRepository.findByEmail(userDTO.email());
//
//        if (!user.getEmail().equals(userDTO.email()) && userRepository.existsByEmail(userDTO.email())) {
//            log.error("User with email already exists: {}", userDTO.email());
//            throw new ConflictRequestException("User with email already exists: " + userDTO.email());
//        }
//
//        if (!user.getPhone().equals(userDTO.phone()) && userRepository.existsByPhone(userDTO.phone())) {
//            log.error("User with phone already exists: {}", userDTO.phone());
//            throw new ConflictRequestException("User with phone already exists: " + userDTO.phone());
//        }
//
//        user.setName(userDTO.name());
//        user.setEmail(userDTO.email());
//        user.setPhone(userDTO.phone());
//        user.setLastUpdatedAt(LocalDateTime.now());
//
//        log.info("User updated successfully: {}", user.getEmail());

        return null;
    }

    private Users getUserLogged() {
        var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.error("User not logged in");
            throw new BadRequestException("User not logged in");
        }

        return user.get();
    }
}
