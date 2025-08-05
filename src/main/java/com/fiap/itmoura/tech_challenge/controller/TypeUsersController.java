package com.fiap.itmoura.tech_challenge.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge.model.dto.OnUpdate;
import com.fiap.itmoura.tech_challenge.model.dto.TypeUsersDTO;
import com.fiap.itmoura.tech_challenge.service.TypeUsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/type-users")
@Tag(name = "Tipos de Usuário", description = "Operações relacionadas aos tipos de usuário")
public class TypeUsersController {

    @Autowired
    private TypeUsersService typeUsersService;

    @GetMapping
    @Operation(summary = "Listar todos os tipos de usuário", description = "Retorna uma lista com todos os tipos de usuário ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de usuário retornada com sucesso")
    })
    public ResponseEntity<List<TypeUsersDTO>> findAll() {
        List<TypeUsersDTO> typeUsers = typeUsersService.findAll();
        return ResponseEntity.ok(typeUsers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de usuário por ID", description = "Retorna um tipo de usuário específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuário encontrado"),
        @ApiResponse(responseCode = "400", description = "Tipo de usuário não encontrado")
    })
    public ResponseEntity<TypeUsersDTO> findById(
            @Parameter(description = "ID do tipo de usuário") @PathVariable UUID id) {
        TypeUsersDTO typeUser = typeUsersService.findById(id);
        return ResponseEntity.ok(typeUser);
    }

    @PostMapping
    @Operation(summary = "Criar novo tipo de usuário", description = "Cria um novo tipo de usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo de usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Tipo de usuário já existe")
    })
    public ResponseEntity<TypeUsersDTO> create(
            @Validated(OnCreate.class) @RequestBody TypeUsersDTO typeUsersDTO) {
        TypeUsersDTO createdTypeUser = typeUsersService.create(typeUsersDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTypeUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tipo de usuário", description = "Atualiza um tipo de usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou tipo de usuário não encontrado"),
        @ApiResponse(responseCode = "409", description = "Nome já existe")
    })
    public ResponseEntity<TypeUsersDTO> update(
            @Parameter(description = "ID do tipo de usuário") @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody TypeUsersDTO typeUsersDTO) {
        TypeUsersDTO updatedTypeUser = typeUsersService.update(id, typeUsersDTO);
        return ResponseEntity.ok(updatedTypeUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tipo de usuário", description = "Exclui um tipo de usuário (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo de usuário excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Tipo de usuário não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do tipo de usuário") @PathVariable UUID id) {
        typeUsersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
