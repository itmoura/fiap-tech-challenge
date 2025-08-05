package com.fiap.itmoura.tech_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.itmoura.tech_challenge.exception.BadRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.MenuItemDTO;
import com.fiap.itmoura.tech_challenge.model.entity.MenuItem;
import com.fiap.itmoura.tech_challenge.model.entity.Restaurant;
import com.fiap.itmoura.tech_challenge.repository.MenuItemRepository;
import com.fiap.itmoura.tech_challenge.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    private MenuItem menuItem;
    private MenuItemDTO menuItemDTO;
    private Restaurant restaurant;
    private UUID menuItemId;
    private UUID restaurantId;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();
        restaurantId = UUID.randomUUID();

        restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Restaurante do João")
                .isActive(true)
                .build();

        menuItem = MenuItem.builder()
                .id(menuItemId)
                .name("Pizza Margherita")
                .description("Pizza com molho de tomate, mussarela e manjericão")
                .price(new BigDecimal("29.90"))
                .category("Pizza")
                .isAvailable(true)
                .preparationTime(30)
                .restaurant(restaurant)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        menuItemDTO = MenuItemDTO.builder()
                .id(menuItemId)
                .name("Pizza Margherita")
                .description("Pizza com molho de tomate, mussarela e manjericão")
                .price(new BigDecimal("29.90"))
                .category("Pizza")
                .isAvailable(true)
                .preparationTime(30)
                .restaurantId(restaurantId)
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfMenuItems() {
        // Arrange
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(menuItemRepository.findAllActive()).thenReturn(menuItems);

        // Act
        List<MenuItemDTO> result = menuItemService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menuItem.getName(), result.get(0).getName());
        verify(menuItemRepository).findAllActive();
    }

    @Test
    void findById_WhenMenuItemExists_ShouldReturnMenuItem() {
        // Arrange
        when(menuItemRepository.findByIdAndActive(menuItemId)).thenReturn(Optional.of(menuItem));

        // Act
        MenuItemDTO result = menuItemService.findById(menuItemId);

        // Assert
        assertNotNull(result);
        assertEquals(menuItem.getName(), result.getName());
        assertEquals(menuItem.getPrice(), result.getPrice());
        verify(menuItemRepository).findByIdAndActive(menuItemId);
    }

    @Test
    void findById_WhenMenuItemNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(menuItemRepository.findByIdAndActive(menuItemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> menuItemService.findById(menuItemId));
        verify(menuItemRepository).findByIdAndActive(menuItemId);
    }

    @Test
    void create_WhenValidData_ShouldCreateMenuItem() {
        // Arrange
        when(restaurantRepository.findByIdAndActive(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        // Act
        MenuItemDTO result = menuItemService.create(menuItemDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO.getName(), result.getName());
        assertEquals(menuItemDTO.getPrice(), result.getPrice());
        verify(restaurantRepository).findByIdAndActive(restaurantId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void create_WhenRestaurantNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(restaurantRepository.findByIdAndActive(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> menuItemService.create(menuItemDTO));
        verify(restaurantRepository).findByIdAndActive(restaurantId);
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void findByRestaurantId_ShouldReturnMenuItemsByRestaurant() {
        // Arrange
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(menuItemRepository.findByRestaurantIdAndActive(restaurantId)).thenReturn(menuItems);

        // Act
        List<MenuItemDTO> result = menuItemService.findByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menuItem.getName(), result.get(0).getName());
        verify(menuItemRepository).findByRestaurantIdAndActive(restaurantId);
    }

    @Test
    void findAvailableByRestaurantId_ShouldReturnAvailableMenuItems() {
        // Arrange
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(menuItemRepository.findByRestaurantIdAndActiveAndAvailable(restaurantId)).thenReturn(menuItems);

        // Act
        List<MenuItemDTO> result = menuItemService.findAvailableByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menuItem.getName(), result.get(0).getName());
        assertTrue(result.get(0).getIsAvailable());
        verify(menuItemRepository).findByRestaurantIdAndActiveAndAvailable(restaurantId);
    }

    @Test
    void findByCategory_ShouldReturnMenuItemsByCategory() {
        // Arrange
        String category = "Pizza";
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        when(menuItemRepository.findByCategoryIgnoreCaseAndActive(category)).thenReturn(menuItems);

        // Act
        List<MenuItemDTO> result = menuItemService.findByCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menuItem.getCategory(), result.get(0).getCategory());
        verify(menuItemRepository).findByCategoryIgnoreCaseAndActive(category);
    }

    @Test
    void update_WhenValidData_ShouldUpdateMenuItem() {
        // Arrange
        when(menuItemRepository.findByIdAndActive(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        // Act
        MenuItemDTO result = menuItemService.update(menuItemId, menuItemDTO);

        // Assert
        assertNotNull(result);
        assertEquals(menuItemDTO.getName(), result.getName());
        verify(menuItemRepository).findByIdAndActive(menuItemId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void updateAvailability_WhenMenuItemExists_ShouldUpdateAvailability() {
        // Arrange
        when(menuItemRepository.findByIdAndActive(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        // Act
        MenuItemDTO result = menuItemService.updateAvailability(menuItemId, false);

        // Assert
        assertNotNull(result);
        verify(menuItemRepository).findByIdAndActive(menuItemId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void delete_WhenMenuItemExists_ShouldDeactivateMenuItem() {
        // Arrange
        when(menuItemRepository.findByIdAndActive(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        // Act
        menuItemService.delete(menuItemId);

        // Assert
        verify(menuItemRepository).findByIdAndActive(menuItemId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void delete_WhenMenuItemNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(menuItemRepository.findByIdAndActive(menuItemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> menuItemService.delete(menuItemId));
        verify(menuItemRepository).findByIdAndActive(menuItemId);
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }
}
