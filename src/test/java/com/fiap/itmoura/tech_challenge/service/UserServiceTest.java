package com.fiap.itmoura.tech_challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fiap.itmoura.tech_challenge.exception.BadRequestException;
import com.fiap.itmoura.tech_challenge.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.AddressDTO;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.model.entity.Address;
import com.fiap.itmoura.tech_challenge.model.entity.TypeUsers;
import com.fiap.itmoura.tech_challenge.model.entity.Users;
import com.fiap.itmoura.tech_challenge.repository.TypeUsersRepository;
import com.fiap.itmoura.tech_challenge.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TypeUsersRepository typeUsersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Users user;
    private UserDTO userDTO;
    private TypeUsers typeUser;
    private Address address;
    private AddressDTO addressDTO;
    private UUID userId;
    private UUID typeUserId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        typeUserId = UUID.randomUUID();

        typeUser = TypeUsers.builder()
                .id(typeUserId)
                .name("Cliente")
                .description("Usuário cliente")
                .isActive(true)
                .build();

        address = Address.builder()
                .id(UUID.randomUUID())
                .street("Rua das Flores")
                .number(123)
                .city("São Paulo")
                .state("SP")
                .zipCode("01234-567")
                .build();

        addressDTO = new AddressDTO(
                address.getId(),
                "Rua das Flores",
                "Centro",
                "Apt 101",
                123,
                "São Paulo",
                "SP",
                "01234-567"
        );

        user = Users.builder()
                .id(userId)
                .name("João Silva")
                .email("joao@email.com")
                .password("encodedPassword")
                .phone("(11) 99999-9999")
                .birthDate(LocalDate.of(1990, 1, 1))
                .typeUser(typeUser)
                .address(address)
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        userDTO = new UserDTO(
                userId,
                "João Silva",
                "joao@email.com",
                "password123",
                null,
                addressDTO,
                LocalDate.of(1990, 1, 1),
                "(11) 99999-9999",
                typeUserId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void findAll_ShouldReturnListOfActiveUsers() {
        // Arrange
        List<Users> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDTO> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getName(), result.get(0).name());
        verify(userRepository).findAll();
    }

    @Test
    void findAllPaginated_ShouldReturnPageOfUsers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Users> userPage = new PageImpl<>(Arrays.asList(user));
        when(userRepository.findByIsActiveTrue(pageable)).thenReturn(userPage);

        // Act
        Page<UserDTO> result = userService.findAllPaginated(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(user.getName(), result.getContent().get(0).name());
        verify(userRepository).findByIsActiveTrue(pageable);
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_WhenUserNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.findById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_WhenUserIsInactive_ShouldThrowBadRequestException() {
        // Arrange
        user.setIsActive(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.findById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.findByEmail("joao@email.com");

        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.email());
        verify(userRepository).findByEmail("joao@email.com");
    }

    @Test
    void findByTypeUserId_ShouldReturnUsersOfType() {
        // Arrange
        List<Users> users = Arrays.asList(user);
        when(userRepository.findByTypeUserIdAndIsActiveTrue(typeUserId)).thenReturn(users);

        // Act
        List<UserDTO> result = userService.findByTypeUserId(typeUserId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getName(), result.get(0).name());
        verify(userRepository).findByTypeUserIdAndIsActiveTrue(typeUserId);
    }

    @Test
    void create_WhenValidData_ShouldCreateUser() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(false);
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.of(typeUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenReturn(user);

        // Act
        UserDTO result = userService.create(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO.name(), result.name());
        assertEquals(userDTO.email(), result.email());
        verify(userRepository).existsByEmail(userDTO.email());
        verify(userRepository).existsByPhone(userDTO.phone());
        verify(typeUsersRepository).findById(typeUserId);
        verify(passwordEncoder).encode(userDTO.password());
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void create_WhenEmailAlreadyExists_ShouldThrowConflictRequestException() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictRequestException.class, () -> userService.create(userDTO));
        verify(userRepository).existsByEmail(userDTO.email());
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void create_WhenPhoneAlreadyExists_ShouldThrowConflictRequestException() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictRequestException.class, () -> userService.create(userDTO));
        verify(userRepository).existsByPhone(userDTO.phone());
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void create_WhenTypeUserNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(false);
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.create(userDTO));
        verify(typeUsersRepository).findById(typeUserId);
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void create_WhenTypeUserIsInactive_ShouldThrowBadRequestException() {
        // Arrange
        typeUser.setIsActive(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(false);
        when(typeUsersRepository.findById(typeUserId)).thenReturn(Optional.of(typeUser));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.create(userDTO));
        verify(typeUsersRepository).findById(typeUserId);
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void update_WhenValidData_ShouldUpdateUser() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        // Act
        UserDTO result = userService.update(userId, userDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void update_WhenUserNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.update(userId, userDTO));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void update_WhenUserIsInactive_ShouldThrowBadRequestException() {
        // Arrange
        user.setIsActive(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.update(userId, userDTO));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void delete_WhenUserExists_ShouldDeactivateUser() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(Users.class))).thenReturn(user);

        // Act
        userService.delete(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void delete_WhenUserNotExists_ShouldThrowBadRequestException() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.delete(userId));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void activate_WhenUserExists_ShouldActivateUser() {
        // Arrange
        user.setIsActive(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(Users.class))).thenReturn(user);

        // Act
        userService.activate(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(Users.class));
        assertTrue(user.getIsActive());
    }

    @Test
    void changePassword_WhenValidData_ShouldChangePassword() {
        // Arrange
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(Users.class))).thenReturn(user);

        // Act
        UserDTO result = userService.changePassword(userId, currentPassword, newPassword);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(passwordEncoder).matches(currentPassword, user.getPassword());
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void changePassword_WhenCurrentPasswordIncorrect_ShouldThrowBadRequestException() {
        // Arrange
        String currentPassword = "wrongPassword";
        String newPassword = "newPassword";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(BadRequestException.class, 
            () -> userService.changePassword(userId, currentPassword, newPassword));
        verify(passwordEncoder).matches(currentPassword, user.getPassword());
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void countActiveUsers_ShouldReturnCount() {
        // Arrange
        when(userRepository.countByIsActiveTrue()).thenReturn(5L);

        // Act
        long result = userService.countActiveUsers();

        // Assert
        assertEquals(5L, result);
        verify(userRepository).countByIsActiveTrue();
    }

    @Test
    void countUsersByType_ShouldReturnCount() {
        // Arrange
        when(userRepository.countByTypeUserIdAndIsActiveTrue(typeUserId)).thenReturn(3L);

        // Act
        long result = userService.countUsersByType(typeUserId);

        // Assert
        assertEquals(3L, result);
        verify(userRepository).countByTypeUserIdAndIsActiveTrue(typeUserId);
    }
}
