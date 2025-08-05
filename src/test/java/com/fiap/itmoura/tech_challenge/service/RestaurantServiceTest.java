package com.fiap.itmoura.tech_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.fiap.itmoura.tech_challenge.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.RestaurantDTO;
import com.fiap.itmoura.tech_challenge.model.entity.Restaurant;
import com.fiap.itmoura.tech_challenge.model.entity.Users;
import com.fiap.itmoura.tech_challenge.repository.RestaurantRepository;
import com.fiap.itmoura.tech_challenge.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private RestaurantDTO restaurantDTO;
    private Users owner;
    private UUID restaurantId;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        ownerId = UUID.randomUUID();

        owner = Users.builder()
                .id(ownerId)
                .name("João Silva")
                .email("joao@email.com")
                .build();

        restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Restaurante do João")
                .description("Especializado em comida italiana")
                .cuisine("Italiana")
                .cnpj("12.345.678/0001-90")
                .phone("(11) 99999-9999")
                .email("contato@restaurante.com")
                .openingTime(LocalTime.of(8, 0))
                .closingTime(LocalTime.of(22, 0))
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        restaurantDTO = RestaurantDTO.builder()
                .id(restaurantId)
                .name("Restaurante do João")
                .description("Especializado em comida italiana")
                .cuisine("Italiana")
                .cnpj("12.345.678/0001-90")
                .phone("(11) 99999-9999")
                .email("contato@restaurante.com")
                .openingTime(LocalTime.of(8, 0))
                .closingTime(LocalTime.of(22, 0))
                .ownerId(ownerId)
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfRestaurants() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        when(restaurantRepository.findAllActive()).thenReturn(restaurants);

        // Act
        List<RestaurantDTO> result = restaurantService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurant.getName(), result.get(0).getName());
        verify(restaurantRepository).findAllActive();
    }

    @Test
    void findById_WhenRestaurantExists_ShouldReturnRestaurant() {
        // Arrange
        when(restaurantRepository.findByIdAndActive(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        RestaurantDTO result = restaurantService.findById(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurant.getName(), result.getName());
        assertEquals(restaurant.getCuisine(), result.getCuisine());
        verify(restaurantRepository).findByIdAndActive(restaurantId);
    }

    @Test
    void findById_WhenRestaurantNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(restaurantRepository.findByIdAndActive(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> restaurantService.findById(restaurantId));
        verify(restaurantRepository).findByIdAndActive(restaurantId);
    }

    @Test
    void create_WhenValidData_ShouldCreateRestaurant() {
        // Arrange
        when(restaurantRepository.existsByCnpj(anyString())).thenReturn(false);
        when(restaurantRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        RestaurantDTO result = restaurantService.create(restaurantDTO);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantDTO.getName(), result.getName());
        assertEquals(restaurantDTO.getCnpj(), result.getCnpj());
        verify(restaurantRepository).existsByCnpj(restaurantDTO.getCnpj());
        verify(restaurantRepository).existsByEmail(restaurantDTO.getEmail());
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void create_WhenCnpjAlreadyExists_ShouldThrowConflictRequestException() {
        // Arrange
        when(restaurantRepository.existsByCnpj(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictRequestException.class, () -> restaurantService.create(restaurantDTO));
        verify(restaurantRepository).existsByCnpj(restaurantDTO.getCnpj());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void create_WhenEmailAlreadyExists_ShouldThrowConflictRequestException() {
        // Arrange
        when(restaurantRepository.existsByCnpj(anyString())).thenReturn(false);
        when(restaurantRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictRequestException.class, () -> restaurantService.create(restaurantDTO));
        verify(restaurantRepository).existsByCnpj(restaurantDTO.getCnpj());
        verify(restaurantRepository).existsByEmail(restaurantDTO.getEmail());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void create_WhenOwnerNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(restaurantRepository.existsByCnpj(anyString())).thenReturn(false);
        when(restaurantRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> restaurantService.create(restaurantDTO));
        verify(userRepository).findById(ownerId);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void findByOwnerId_ShouldReturnRestaurantsByOwner() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        when(restaurantRepository.findByOwnerIdAndActive(ownerId)).thenReturn(restaurants);

        // Act
        List<RestaurantDTO> result = restaurantService.findByOwnerId(ownerId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurant.getName(), result.get(0).getName());
        verify(restaurantRepository).findByOwnerIdAndActive(ownerId);
    }

    @Test
    void findByCuisine_ShouldReturnRestaurantsByCuisine() {
        // Arrange
        String cuisine = "Italiana";
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        when(restaurantRepository.findByCuisineContainingIgnoreCaseAndActive(cuisine)).thenReturn(restaurants);

        // Act
        List<RestaurantDTO> result = restaurantService.findByCuisine(cuisine);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurant.getCuisine(), result.get(0).getCuisine());
        verify(restaurantRepository).findByCuisineContainingIgnoreCaseAndActive(cuisine);
    }

    @Test
    void delete_WhenRestaurantExists_ShouldDeactivateRestaurant() {
        // Arrange
        when(restaurantRepository.findByIdAndActive(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        restaurantService.delete(restaurantId);

        // Assert
        verify(restaurantRepository).findByIdAndActive(restaurantId);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void delete_WhenRestaurantNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(restaurantRepository.findByIdAndActive(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> restaurantService.delete(restaurantId));
        verify(restaurantRepository).findByIdAndActive(restaurantId);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}
