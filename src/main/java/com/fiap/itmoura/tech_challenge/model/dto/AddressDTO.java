package com.fiap.itmoura.tech_challenge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AddressDTO(

        @Schema(title = "street", description = "Street name", example = "Rua das Flores")
        String street,

        @Schema(title = "neighborhood", description = "Neighborhood name", example = "Jardim das Rosas")
        String neighborhood,

        @Schema(title = "complement", description = "Address complement", example = "Apt 101")
        String complement,

        @Schema(title = "number", description = "House number", example = "123")
        String number,

        @Schema(title = "city", description = "City name", example = "São Paulo")
        String city,

        @Schema(title = "state", description = "State name", example = "SP")
        String state,

        @Schema(title = "zipCode", description = "ZIP code", example = "12345-678")
        @NotNull
        String zipCode
) {
}
