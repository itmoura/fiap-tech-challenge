package com.fiap.itmoura.tech_challenge.model.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
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
@Schema(description = "DTO para Restaurante")
public class RestaurantDTO {

    @Schema(description = "ID do restaurante", example = "123e4567-e89b-12d3-a456-426614174000")
    @Null(groups = OnCreate.class, message = "ID deve ser nulo na criação")
    @NotNull(groups = OnUpdate.class, message = "ID é obrigatório na atualização")
    private UUID id;

    @Schema(description = "Nome do restaurante", example = "Restaurante do João")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Schema(description = "Descrição do restaurante", example = "Especializado em comida italiana")
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    @Schema(description = "Tipo de culinária", example = "Italiana")
    @NotBlank(message = "Tipo de culinária é obrigatório")
    @Size(max = 50, message = "Tipo de culinária deve ter no máximo 50 caracteres")
    private String cuisine;

    @Schema(description = "CNPJ do restaurante", example = "12.345.678/0001-90")
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX")
    private String cnpj;

    @Schema(description = "Telefone do restaurante", example = "(11) 99999-9999")
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
    private String phone;

    @Schema(description = "Email do restaurante", example = "contato@restaurante.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String email;

    @Schema(description = "Horário de abertura", example = "08:00:00")
    private LocalTime openingTime;

    @Schema(description = "Horário de fechamento", example = "22:00:00")
    private LocalTime closingTime;

    @Schema(description = "Endereço do restaurante")
    @Valid
    private AddressDTO address;

    @Schema(description = "ID do proprietário", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "ID do proprietário é obrigatório")
    private UUID ownerId;

    @Schema(description = "Itens do cardápio")
    private List<MenuItemDTO> menuItems;

    @Schema(description = "Data de criação", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Status ativo", example = "true")
    private Boolean isActive;
}
