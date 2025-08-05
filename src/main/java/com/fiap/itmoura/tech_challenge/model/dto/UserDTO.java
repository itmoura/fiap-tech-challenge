package com.fiap.itmoura.tech_challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fiap.itmoura.tech_challenge.model.entity.Users;
import com.fiap.itmoura.tech_challenge.model.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDTO(

        @Schema(title = "id", description = "User ID", example = "12345")
        UUID id,

        @Schema(title = "name", description = "User name", example = "John Doe")
        @NotNull( message = "Name is required", groups = OnCreate.class)
        String name,

        @Schema(title = "email", description = "User email", example = "italo@meuemail.com")
        @NotNull(message = "Email is required", groups = OnCreate.class)
        String email,

        @Schema(title = "password", description = "User password", example = "123456")
        @NotNull(message = "Password is required", groups = OnCreate.class)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        @Schema(title = "roles", description = "User role", example = "ADMIN")
        Set<UserRoleEnum> roles,

        @Schema(title = "address", description = "User address")
        @Valid AddressDTO address,

        @Schema(title = "birthDate", description = "User birth date", example = "1990-01-01")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthDate,

        @Schema(title = "phone", description = "User phone number", example = "+5511999999999")
        @NotNull(message = "Phone number is required", groups = OnCreate.class)
        String phone,

        @Schema(title = "typeUserId", description = "Type user ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID typeUserId,

        @Schema(title = "lastUpdate", description = "Last update timestamp", example = "2023-10-01T12:00:00Z")
        @LastModifiedDate
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime lastUpdate,

        @Schema(title = "createdAt", description = "Creation timestamp", example = "2023-10-01T12:00:00Z")
        @CreatedDate
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt
) {
        public Users toEntity() {
            return Users.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .address(address != null ? address.toEntity() : null)
                    .birthDate(birthDate)
                    .phone(phone)
                    .lastUpdatedAt(LocalDateTime.now())
                    .createdAt(createdAt != null ? createdAt : LocalDateTime.now())
                    .build();
        }

        public static UserDTO fromEntity(Users users) {
            return new UserDTO(
                    users.getId(),
                    users.getName(),
                    users.getEmail(),
                    users.getPassword(),
                    null, // roles removido temporariamente
                    users.getAddress() != null ? AddressDTO.fromEntity(users.getAddress()) : null,
                    users.getBirthDate(),
                    users.getPhone(),
                    users.getTypeUser() != null ? users.getTypeUser().getId() : null,
                    users.getLastUpdatedAt(),
                    users.getCreatedAt()
            );
        }
}
