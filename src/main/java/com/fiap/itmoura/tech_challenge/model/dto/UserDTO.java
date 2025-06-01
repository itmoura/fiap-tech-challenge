package com.fiap.itmoura.tech_challenge.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fiap.itmoura.tech_challenge.model.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

public record UserDTO(

        @Schema(title = "name", description = "User name", example = "John Doe")
        @NotNull
        String name,

        @Schema(title = "email", description = "User email", example = "italo@meuemail.com")
        @NotNull
        String email,

        @Schema(title = "password", description = "User password", example = "123456")
        @NotNull
        String password,

        @Schema(title = "role", description = "User role", example = "ADMIN")
        Set<UserRoleEnum> role,

        @Schema(title = "address", description = "User address")
        AddressDTO address,

        @Schema(title = "phone", description = "User phone number", example = "+5511999999999")
        String phone,

        @Schema(title = "lastUpdate", description = "Last update timestamp", example = "2023-10-01T12:00:00Z")
        @LastModifiedDate
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime lastUpdate,

        @Schema(title = "createdAt", description = "Creation timestamp", example = "2023-10-01T12:00:00Z")
        @CreatedDate
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createdAt
) {
}
