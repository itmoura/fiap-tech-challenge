package com.fiap.itmoura.tech_challenge.controller.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;

import com.fiap.itmoura.tech_challenge.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge.model.dto.OnUpdate;
import com.fiap.itmoura.tech_challenge.model.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuários", description = "Operações relacionadas aos usuários")
public interface UserControllerInterface {

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    List<UserDTO> findAll();

    @GetMapping("/paginated")
    @Operation(summary = "Listar usuários paginados", description = "Retorna uma página de usuários ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de usuários retornada com sucesso")
    })
    Page<UserDTO> findAllPaginated(@PageableDefault(size = 20) Pageable pageable);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    UserDTO findById(@Parameter(description = "ID do usuário") @PathVariable UUID id);

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuário por email", description = "Retorna um usuário específico pelo email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    UserDTO findByEmail(@Parameter(description = "Email do usuário") @PathVariable String email);

    @GetMapping("/type/{typeUserId}")
    @Operation(summary = "Buscar usuários por tipo", description = "Retorna todos os usuários de um tipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    List<UserDTO> findByTypeUserId(@Parameter(description = "ID do tipo de usuário") @PathVariable UUID typeUserId);

    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Email ou telefone já existe")
    })
    UserDTO create(@Validated(OnCreate.class) @RequestBody UserDTO userDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário não encontrado"),
        @ApiResponse(responseCode = "409", description = "Email ou telefone já existe")
    })
    UserDTO update(@Parameter(description = "ID do usuário") @PathVariable UUID id, @Validated(OnUpdate.class) @RequestBody UserDTO userDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar usuário", description = "Desativa um usuário (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    void delete(@Parameter(description = "ID do usuário") @PathVariable UUID id);

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Ativar usuário", description = "Ativa um usuário desativado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário ativado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Usuário não encontrado")
    })
    void activate(@Parameter(description = "ID do usuário") @PathVariable UUID id);

    @PatchMapping("/{id}/change-password")
    @Operation(summary = "Alterar senha", description = "Altera a senha de um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Senha atual incorreta ou usuário não encontrado")
    })
    UserDTO changePassword(@Parameter(description = "ID do usuário") @PathVariable UUID id, @Parameter(description = "Senha atual") @RequestParam String currentPassword, @Parameter(description = "Nova senha") @RequestParam String newPassword);

    @GetMapping("/count")
    @Operation(summary = "Contar usuários ativos", description = "Retorna o número total de usuários ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    Long countActiveUsers();

    @GetMapping("/count/type/{typeUserId}")
    @Operation(summary = "Contar usuários por tipo", description = "Retorna o número de usuários de um tipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    Long countUsersByType(@Parameter(description = "ID do tipo de usuário") @PathVariable UUID typeUserId);

}