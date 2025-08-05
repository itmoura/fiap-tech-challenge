package com.fiap.itmoura.tech_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
import com.fiap.itmoura.tech_challenge.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.TypeUsersDTO;
import com.fiap.itmoura.tech_challenge.model.entity.TypeUsers;
import com.fiap.itmoura.tech_challenge.repository.TypeUsersRepository;

@ExtendWith(MockitoExtension.class)
class TypeUsersServiceTest {

    @Mock
    private TypeUsersRepository typeUsersRepository;

    @InjectMocks
    private TypeUsersService typeUsersService;

    private TypeUsers typeUser;
    private TypeUsersDTO typeUsersDTO;
    private UUID typeUserId;

    @BeforeEach
    void setUp() {
        typeUserId = UUID.randomUUID();
        
        typeUser = TypeUsers.builder()
                .id(typeUserId)
                .name("Dono de Restaurante")
                .description("Usuário responsável por gerenciar restaurante")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        typeUsersDTO = TypeUsersDTO.builder()
                .id(typeUserId)
                .name("Dono de Restaurante")
                .description("Usuário responsável por gerenciar restaurante")
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfTypeUsers() {
        // Arrange
        List<TypeUsers> typeUsersList = Arrays.asList(typeUser);
        when(typeUsersRepository.findAllActive()).thenReturn(typeUsersList);

        // Act
        List<TypeUsersDTO> result = typeUsersService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(typeUser.getName(), result.get(0).getName());
        verify(typeUsersRepository).findAllActive();
    }

    @Test
    void findById_WhenTypeUserExists_ShouldReturnTypeUser() {
        // Arrange
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.of(typeUser));

        // Act
        TypeUsersDTO result = typeUsersService.findById(typeUserId);

        // Assert
        assertNotNull(result);
        assertEquals(typeUser.getName(), result.getName());
        assertEquals(typeUser.getDescription(), result.getDescription());
        verify(typeUsersRepository).findById(typeUserId);
    }

    @Test
    void findById_WhenTypeUserNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> typeUsersService.findById(typeUserId));
        verify(typeUsersRepository).findById(typeUserId);
    }

    @Test
    void findById_WhenTypeUserIsInactive_ShouldThrowBadRequestException() {
        // Arrange
        typeUser.setIsActive(false);
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.of(typeUser));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> typeUsersService.findById(typeUserId));
        verify(typeUsersRepository).findById(typeUserId);
    }

    @Test
    void create_WhenValidData_ShouldCreateTypeUser() {
        // Arrange
        when(typeUsersRepository.existsByName(anyString())).thenReturn(false);
        when(typeUsersRepository.save(any(TypeUsers.class))).thenReturn(typeUser);

        // Act
        TypeUsersDTO result = typeUsersService.create(typeUsersDTO);

        // Assert
        assertNotNull(result);
        assertEquals(typeUsersDTO.getName(), result.getName());
        assertEquals(typeUsersDTO.getDescription(), result.getDescription());
        verify(typeUsersRepository).existsByName(typeUsersDTO.getName());
        verify(typeUsersRepository).save(any(TypeUsers.class));
    }

    @Test
    void create_WhenNameAlreadyExists_ShouldThrowConflictRequestException() {
        // Arrange
        when(typeUsersRepository.existsByName(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictRequestException.class, () -> typeUsersService.create(typeUsersDTO));
        verify(typeUsersRepository).existsByName(typeUsersDTO.getName());
        verify(typeUsersRepository, never()).save(any(TypeUsers.class));
    }

    @Test
    void update_WhenValidData_ShouldUpdateTypeUser() {
        // Arrange
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.of(typeUser));
        when(typeUsersRepository.existsByName(anyString())).thenReturn(false);
        when(typeUsersRepository.save(any(TypeUsers.class))).thenReturn(typeUser);

        // Act
        TypeUsersDTO result = typeUsersService.update(typeUserId, typeUsersDTO);

        // Assert
        assertNotNull(result);
        assertEquals(typeUsersDTO.getName(), result.getName());
        verify(typeUsersRepository).findById(typeUserId);
        verify(typeUsersRepository).save(any(TypeUsers.class));
    }

    @Test
    void update_WhenTypeUserNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> typeUsersService.update(typeUserId, typeUsersDTO));
        verify(typeUsersRepository).findById(typeUserId);
        verify(typeUsersRepository, never()).save(any(TypeUsers.class));
    }

    @Test
    void delete_WhenTypeUserExists_ShouldDeactivateTypeUser() {
        // Arrange
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.of(typeUser));
        when(typeUsersRepository.save(any(TypeUsers.class))).thenReturn(typeUser);

        // Act
        typeUsersService.delete(typeUserId);

        // Assert
        verify(typeUsersRepository).findById(typeUserId);
        verify(typeUsersRepository).save(any(TypeUsers.class));
    }

    @Test
    void delete_WhenTypeUserNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> typeUsersService.delete(typeUserId));
        verify(typeUsersRepository).findById(typeUserId);
        verify(typeUsersRepository, never()).save(any(TypeUsers.class));
    }
}
