package com.fiap.itmoura.tech_challenge.repository;

import com.fiap.itmoura.tech_challenge.model.entity.Users;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Users> findByEmail(String username);
}
