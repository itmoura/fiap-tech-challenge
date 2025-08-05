package com.fiap.itmoura.tech_challenge.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
@Schema(description = "DTO para Item do Cardápio")
public class MenuItemDTO {

    @Schema(description = "ID do item", example = "123e4567-e89b-12d3-a456-426614174000")
    @Null(groups = OnCreate.class, message = "ID deve ser nulo na criação")
    @NotNull(groups = OnUpdate.class, message = "ID é obrigatório na atualização")
    private UUID id;

    @Schema(description = "Nome do item", example = "Pizza Margherita")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Schema(description = "Descrição do item", example = "Pizza com molho de tomate, mussarela e manjericão")
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    @Schema(description = "Preço do item", example = "29.90")
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal price;

    @Schema(description = "Categoria do item", example = "Pizza")
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    private String category;

    @Schema(description = "URL da imagem", example = "https://exemplo.com/pizza.jpg")
    @Size(max = 255, message = "URL da imagem deve ter no máximo 255 caracteres")
    private String imageUrl;

    @Schema(description = "Item disponível", example = "true")
    private Boolean isAvailable;

    @Schema(description = "Tempo de preparo em minutos", example = "30")
    @Min(value = 1, message = "Tempo de preparo deve ser maior que zero")
    private Integer preparationTime;

    @Schema(description = "ID do restaurante", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "ID do restaurante é obrigatório")
    private UUID restaurantId;

    @Schema(description = "Data de criação", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Status ativo", example = "true")
    private Boolean isActive;
}
