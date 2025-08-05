package com.fiap.itmoura.tech_challenge.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiap.itmoura.tech_challenge.exception.BadRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.MenuItemDTO;
import com.fiap.itmoura.tech_challenge.model.entity.MenuItem;
import com.fiap.itmoura.tech_challenge.model.entity.Restaurant;
import com.fiap.itmoura.tech_challenge.repository.MenuItemRepository;
import com.fiap.itmoura.tech_challenge.repository.RestaurantRepository;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<MenuItemDTO> findAll() {
        return menuItemRepository.findAllActive()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public MenuItemDTO findById(UUID id) {
        MenuItem menuItem = menuItemRepository.findByIdAndActive(id)
                .orElseThrow(() -> new BadRequestException("Item do cardápio não encontrado"));
        
        return convertToDTO(menuItem);
    }

    public List<MenuItemDTO> findByRestaurantId(UUID restaurantId) {
        return menuItemRepository.findByRestaurantIdAndActive(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItemDTO> findAvailableByRestaurantId(UUID restaurantId) {
        return menuItemRepository.findByRestaurantIdAndActiveAndAvailable(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItemDTO> findByCategory(String category) {
        return menuItemRepository.findByCategoryIgnoreCaseAndActive(category)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<MenuItemDTO> findByName(String name) {
        return menuItemRepository.findByNameContainingIgnoreCaseAndActive(name)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public MenuItemDTO create(MenuItemDTO menuItemDTO) {
        // Verifica se o restaurante existe
        Restaurant restaurant = restaurantRepository.findByIdAndActive(menuItemDTO.getRestaurantId())
                .orElseThrow(() -> new BadRequestException("Restaurante não encontrado"));

        MenuItem menuItem = MenuItem.builder()
                .name(menuItemDTO.getName())
                .description(menuItemDTO.getDescription())
                .price(menuItemDTO.getPrice())
                .category(menuItemDTO.getCategory())
                .imageUrl(menuItemDTO.getImageUrl())
                .isAvailable(menuItemDTO.getIsAvailable() != null ? menuItemDTO.getIsAvailable() : true)
                .preparationTime(menuItemDTO.getPreparationTime())
                .restaurant(restaurant)
                .isActive(true)
                .build();

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return convertToDTO(savedMenuItem);
    }

    public MenuItemDTO update(UUID id, MenuItemDTO menuItemDTO) {
        MenuItem existingMenuItem = menuItemRepository.findByIdAndActive(id)
                .orElseThrow(() -> new BadRequestException("Item do cardápio não encontrado"));

        // Atualiza os campos
        existingMenuItem.setName(menuItemDTO.getName());
        existingMenuItem.setDescription(menuItemDTO.getDescription());
        existingMenuItem.setPrice(menuItemDTO.getPrice());
        existingMenuItem.setCategory(menuItemDTO.getCategory());
        existingMenuItem.setImageUrl(menuItemDTO.getImageUrl());
        existingMenuItem.setIsAvailable(menuItemDTO.getIsAvailable());
        existingMenuItem.setPreparationTime(menuItemDTO.getPreparationTime());

        MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
        return convertToDTO(updatedMenuItem);
    }

    public void delete(UUID id) {
        MenuItem menuItem = menuItemRepository.findByIdAndActive(id)
                .orElseThrow(() -> new BadRequestException("Item do cardápio não encontrado"));

        menuItem.setIsActive(false);
        menuItemRepository.save(menuItem);
    }

    public MenuItemDTO updateAvailability(UUID id, Boolean isAvailable) {
        MenuItem menuItem = menuItemRepository.findByIdAndActive(id)
                .orElseThrow(() -> new BadRequestException("Item do cardápio não encontrado"));

        menuItem.setIsAvailable(isAvailable);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return convertToDTO(updatedMenuItem);
    }

    private MenuItemDTO convertToDTO(MenuItem menuItem) {
        return MenuItemDTO.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .category(menuItem.getCategory())
                .imageUrl(menuItem.getImageUrl())
                .isAvailable(menuItem.getIsAvailable())
                .preparationTime(menuItem.getPreparationTime())
                .restaurantId(menuItem.getRestaurant().getId())
                .createdAt(menuItem.getCreatedAt())
                .updatedAt(menuItem.getUpdatedAt())
                .isActive(menuItem.getIsActive())
                .build();
    }
}
