package com.fiap.itmoura.tech_challenge.controller.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fiap.itmoura.tech_challenge.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge.model.dto.OnUpdate;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface UserControllerInterface {

    @GetMapping("/all")
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    List<UserDTO> getAllUsers();

    @PostMapping("/create")
    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário com os dados fornecidos")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    UUID createUser(@Validated(OnCreate.class) @RequestBody UserDTO userDTO);

    @GetMapping("/{userId}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    UserDTO getUserById(@PathVariable UUID userId);

    @PutMapping("/update")
    @Operation(summary = "Atualizar um usuário", description = "Atualiza os dados de um usuário existente")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    UserDTO updateUser(@Validated(OnUpdate.class) @RequestBody UserDTO userDTO);

    @DeleteMapping("/{userId}/delete-logic")
    @Operation(summary = "Deletar um usuário logicamente", description = "Deleta um usuário logicamente")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    void deleteLogicUser(@PathVariable UUID userId);

    @DeleteMapping("/{userId}/delete-physical")
    @Operation(summary = "Deletar um usuário fisicamente", description = "Deleta um usuário fisicamente")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    void deletePhysicalUser(@PathVariable UUID userId);
}