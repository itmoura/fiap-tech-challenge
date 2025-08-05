package com.fiap.itmoura.tech_challenge.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge.model.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    
    @Query("SELECT r FROM restaurants r WHERE r.isActive = true")
    List<Restaurant> findAllActive();
    
    @Query("SELECT r FROM restaurants r WHERE r.isActive = true AND r.id = :id")
    Optional<Restaurant> findByIdAndActive(@Param("id") UUID id);
    
    @Query("SELECT r FROM restaurants r WHERE r.isActive = true AND r.owner.id = :ownerId")
    List<Restaurant> findByOwnerIdAndActive(@Param("ownerId") UUID ownerId);
    
    @Query("SELECT r FROM restaurants r WHERE r.isActive = true AND LOWER(r.cuisine) LIKE LOWER(CONCAT('%', :cuisine, '%'))")
    List<Restaurant> findByCuisineContainingIgnoreCaseAndActive(@Param("cuisine") String cuisine);
    
    @Query("SELECT r FROM restaurants r WHERE r.isActive = true AND LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Restaurant> findByNameContainingIgnoreCaseAndActive(@Param("name") String name);
    
    boolean existsByCnpj(String cnpj);
    
    boolean existsByEmail(String email);
}
