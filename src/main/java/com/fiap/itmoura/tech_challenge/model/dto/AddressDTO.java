package com.fiap.itmoura.tech_challenge.model.dto;

import com.fiap.itmoura.tech_challenge.model.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AddressDTO(

        @Schema(title = "street", description = "Street name", example = "Rua das Flores")
        String street,

        @Schema(title = "neighborhood", description = "Neighborhood name", example = "Jardim das Rosas")
        String neighborhood,

        @Schema(title = "complement", description = "Address complement", example = "Apt 101")
        String complement,

        @Schema(title = "number", description = "House number", example = "123")
        Integer number,

        @Schema(title = "city", description = "City name", example = "São Paulo")
        String city,

        @Schema(title = "state", description = "State name", example = "SP")
        String state,

        @Schema(title = "zipCode", description = "ZIP code", example = "12345-678")
        @NotNull(message = "ZIP code cannot be null")
        String zipCode
) {
        public Address toEntity() {
                return Address.builder()
                        .street(street)
                        .neighborhood(neighborhood)
                        .complement(complement)
                        .number(number)
                        .city(city)
                        .state(state)
                        .zipCode(zipCode)
                        .build();
        }

        public static AddressDTO fromEntity(Address address) {
                return new AddressDTO(
                        address.getStreet(),
                        address.getNeighborhood(),
                        address.getComplement(),
                        address.getNumber(),
                        address.getCity(),
                        address.getState(),
                        address.getZipCode()
                );
        }
}
