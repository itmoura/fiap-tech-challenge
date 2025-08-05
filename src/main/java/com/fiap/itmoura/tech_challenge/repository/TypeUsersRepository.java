package com.fiap.itmoura.tech_challenge.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge.model.entity.TypeUsers;

@Repository
public interface TypeUsersRepository extends JpaRepository<TypeUsers, UUID> {
    
    Optional<TypeUsers> findByName(String name);
    
    @Query("SELECT t FROM type_users t WHERE t.isActive = true")
    List<TypeUsers> findAllActive();
    
    boolean existsByName(String name);
}
