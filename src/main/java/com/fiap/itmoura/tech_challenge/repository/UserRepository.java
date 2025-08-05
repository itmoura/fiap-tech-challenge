package com.fiap.itmoura.tech_challenge.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge.model.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    
    Optional<Users> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    Page<Users> findByIsActiveTrue(Pageable pageable);
    
    @Query("SELECT u FROM users u WHERE u.typeUser.id = :typeUserId AND u.isActive = true")
    List<Users> findByTypeUserIdAndIsActiveTrue(@Param("typeUserId") UUID typeUserId);
    
    long countByIsActiveTrue();
    
    @Query("SELECT COUNT(u) FROM users u WHERE u.typeUser.id = :typeUserId AND u.isActive = true")
    long countByTypeUserIdAndIsActiveTrue(@Param("typeUserId") UUID typeUserId);
    
    @Query("SELECT u FROM users u WHERE u.isActive = true AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Users> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);
    
    @Query("SELECT u FROM users u WHERE u.isActive = true AND u.email = :email")
    Optional<Users> findByEmailAndIsActiveTrue(@Param("email") String email);
}
