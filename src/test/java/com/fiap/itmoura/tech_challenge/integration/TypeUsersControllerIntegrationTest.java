package com.fiap.itmoura.tech_challenge.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.itmoura.tech_challenge.model.dto.TypeUsersDTO;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
class TypeUsersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTypeUser_WhenValidData_ShouldReturnCreated() throws Exception {
        // Arrange
        TypeUsersDTO typeUsersDTO = TypeUsersDTO.builder()
                .name("Dono de Restaurante")
                .description("Usuário responsável por gerenciar restaurante")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/type-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(typeUsersDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dono de Restaurante"))
                .andExpect(jsonPath("$.description").value("Usuário responsável por gerenciar restaurante"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void createTypeUser_WhenInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        TypeUsersDTO typeUsersDTO = TypeUsersDTO.builder()
                .name("") // Nome vazio
                .description("Descrição")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/type-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(typeUsersDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTypeUsers_ShouldReturnOk() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/type-users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getTypeUserById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(get("/api/type-users/{id}", nonExistentId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTypeUser_WhenInvalidId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        TypeUsersDTO typeUsersDTO = TypeUsersDTO.builder()
                .id(nonExistentId)
                .name("Novo Nome")
                .description("Nova Descrição")
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/type-users/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(typeUsersDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTypeUser_WhenInvalidId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(delete("/api/type-users/{id}", nonExistentId))
                .andExpected(status().isBadRequest());
    }
}
