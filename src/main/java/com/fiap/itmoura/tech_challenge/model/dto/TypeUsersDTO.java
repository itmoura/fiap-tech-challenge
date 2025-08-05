package com.fiap.itmoura.tech_challenge.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para Tipo de Usuário")
public class TypeUsersDTO {

    @Schema(description = "ID do tipo de usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    @Null(groups = OnCreate.class, message = "ID deve ser nulo na criação")
    @NotNull(groups = OnUpdate.class, message = "ID é obrigatório na atualização")
    private UUID id;

    @Schema(description = "Nome do tipo de usuário", example = "Dono de Restaurante")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String name;

    @Schema(description = "Descrição do tipo de usuário", example = "Usuário responsável por gerenciar restaurante")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String description;

    @Schema(description = "Data de criação", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Status ativo", example = "true")
    private Boolean isActive;
}
