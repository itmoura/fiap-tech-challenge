package com.fiap.itmoura.tech_challenge.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.itmoura.tech_challenge.model.dto.AddressDTO;
import com.fiap.itmoura.tech_challenge.model.dto.TypeUsersDTO;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.model.entity.TypeUsers;
import com.fiap.itmoura.tech_challenge.repository.TypeUsersRepository;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TypeUsersRepository typeUsersRepository;

    private TypeUsers typeUser;
    private UserDTO userDTO;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        // Criar tipo de usuário para os testes
        typeUser = TypeUsers.builder()
                .name("Cliente Teste")
                .description("Tipo de usuário para testes")
                .isActive(true)
                .build();
        typeUser = typeUsersRepository.save(typeUser);

        // Criar endereço para os testes
        addressDTO = new AddressDTO(
                null,
                "Rua das Flores",
                "Centro",
                "Apt 101",
                123,
                "São Paulo",
                "SP",
                "01234-567"
        );

        // Criar usuário para os testes
        userDTO = new UserDTO(
                null,
                "João Silva",
                "joao.teste@email.com",
                "password123",
                null,
                addressDTO,
                LocalDate.of(1990, 1, 1),
                "(11) 99999-9999",
                typeUser.getId(),
                null,
                null
        );
    }

    @Test
    void createUser_WhenValidData_ShouldReturnCreated() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.teste@email.com"))
                .andExpect(jsonPath("$.phone").value("(11) 99999-9999"))
                .andExpect(jsonPath("$.typeUserId").value(typeUser.getId().toString()));
    }

    @Test
    void createUser_WhenInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        UserDTO invalidUserDTO = new UserDTO(
                null,
                "", // Nome vazio
                "invalid-email", // Email inválido
                null, // Senha nula
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllUsersPaginated_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/paginated")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.pageable").exists());
    }

    @Test
    void getUserById_WhenNotExists_ShouldReturnBadRequest() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}", nonExistentId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserByEmail_WhenNotExists_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/email/{email}", "naoexiste@email.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsersByType_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/type/{typeUserId}", typeUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateUser_WhenInvalidId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        UserDTO updateUserDTO = new UserDTO(
                nonExistentId,
                "Nome Atualizado",
                "novo@email.com",
                null,
                null,
                null,
                null,
                "(11) 88888-8888",
                null,
                null,
                null
        );

        // Act & Assert
        mockMvc.perform(put("/api/users/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_WhenInvalidId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", nonExistentId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activateUser_WhenInvalidId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(patch("/api/users/{id}/activate", nonExistentId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changePassword_WhenInvalidId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(patch("/api/users/{id}/change-password", nonExistentId)
                .param("currentPassword", "currentPass")
                .param("newPassword", "newPass"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void countActiveUsers_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void countUsersByType_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/count/type/{typeUserId}", typeUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void createAndRetrieveUser_FullFlow_ShouldWork() throws Exception {
        // Criar usuário
        String createResponse = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO createdUser = objectMapper.readValue(createResponse, UserDTO.class);

        // Buscar usuário criado
        mockMvc.perform(get("/api/users/{id}", createdUser.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.teste@email.com"));

        // Buscar por email
        mockMvc.perform(get("/api/users/email/{email}", createdUser.email()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Silva"));

        // Atualizar usuário
        UserDTO updateUserDTO = new UserDTO(
                createdUser.id(),
                "João Silva Atualizado",
                createdUser.email(),
                null,
                null,
                null,
                null,
                createdUser.phone(),
                null,
                null,
                null
        );

        mockMvc.perform(put("/api/users/{id}", createdUser.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Silva Atualizado"));

        // Desativar usuário
        mockMvc.perform(delete("/api/users/{id}", createdUser.id()))
                .andExpect(status().isNoContent());

        // Verificar se usuário foi desativado
        mockMvc.perform(get("/api/users/{id}", createdUser.id()))
                .andExpect(status().isBadRequest());
    }
}
