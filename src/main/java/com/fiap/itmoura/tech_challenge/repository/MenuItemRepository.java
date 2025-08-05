package com.fiap.itmoura.tech_challenge.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge.model.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    
    @Query("SELECT m FROM menu_items m WHERE m.isActive = true")
    List<MenuItem> findAllActive();
    
    @Query("SELECT m FROM menu_items m WHERE m.isActive = true AND m.id = :id")
    Optional<MenuItem> findByIdAndActive(@Param("id") UUID id);
    
    @Query("SELECT m FROM menu_items m WHERE m.isActive = true AND m.restaurant.id = :restaurantId")
    List<MenuItem> findByRestaurantIdAndActive(@Param("restaurantId") UUID restaurantId);
    
    @Query("SELECT m FROM menu_items m WHERE m.isActive = true AND m.isAvailable = true AND m.restaurant.id = :restaurantId")
    List<MenuItem> findByRestaurantIdAndActiveAndAvailable(@Param("restaurantId") UUID restaurantId);
    
    @Query("SELECT m FROM menu_items m WHERE m.isActive = true AND LOWER(m.category) = LOWER(:category)")
    List<MenuItem> findByCategoryIgnoreCaseAndActive(@Param("category") String category);
    
    @Query("SELECT m FROM menu_items m WHERE m.isActive = true AND LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MenuItem> findByNameContainingIgnoreCaseAndActive(@Param("name") String name);
}
