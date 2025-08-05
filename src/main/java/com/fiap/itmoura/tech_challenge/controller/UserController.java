package com.fiap.itmoura.tech_challenge.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge.model.dto.OnUpdate;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;
import com.fiap.itmoura.tech_challenge.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Operações relacionadas aos usuários")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Listar usuários paginados", description = "Retorna uma página de usuários ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de usuários retornada com sucesso")
    })
    public ResponseEntity<Page<UserDTO>> findAllPaginated(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserDTO> users = userService.findAllPaginated(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    public ResponseEntity<UserDTO> findById(
            @Parameter(description = "ID do usuário") @PathVariable UUID id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuário por email", description = "Retorna um usuário específico pelo email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    public ResponseEntity<UserDTO> findByEmail(
            @Parameter(description = "Email do usuário") @PathVariable String email) {
        UserDTO user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/type/{typeUserId}")
    @Operation(summary = "Buscar usuários por tipo", description = "Retorna todos os usuários de um tipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    public ResponseEntity<List<UserDTO>> findByTypeUserId(
            @Parameter(description = "ID do tipo de usuário") @PathVariable UUID typeUserId) {
        List<UserDTO> users = userService.findByTypeUserId(typeUserId);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Email ou telefone já existe")
    })
    public ResponseEntity<UserDTO> create(
            @Validated(OnCreate.class) @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.create(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário não encontrado"),
        @ApiResponse(responseCode = "409", description = "Email ou telefone já existe")
    })
    public ResponseEntity<UserDTO> update(
            @Parameter(description = "ID do usuário") @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.update(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar usuário", description = "Desativa um usuário (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do usuário") @PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Ativar usuário", description = "Ativa um usuário desativado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário ativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> activate(
            @Parameter(description = "ID do usuário") @PathVariable UUID id) {
        userService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/change-password")
    @Operation(summary = "Alterar senha", description = "Altera a senha de um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Senha atual incorreta ou usuário não encontrado")
    })
    public ResponseEntity<UserDTO> changePassword(
            @Parameter(description = "ID do usuário") @PathVariable UUID id,
            @Parameter(description = "Senha atual") @RequestParam String currentPassword,
            @Parameter(description = "Nova senha") @RequestParam String newPassword) {
        UserDTO user = userService.changePassword(id, currentPassword, newPassword);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/count")
    @Operation(summary = "Contar usuários ativos", description = "Retorna o número total de usuários ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> countActiveUsers() {
        long count = userService.countActiveUsers();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/type/{typeUserId}")
    @Operation(summary = "Contar usuários por tipo", description = "Retorna o número de usuários de um tipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> countUsersByType(
            @Parameter(description = "ID do tipo de usuário") @PathVariable UUID typeUserId) {
        long count = userService.countUsersByType(typeUserId);
        return ResponseEntity.ok(count);
    }
}
