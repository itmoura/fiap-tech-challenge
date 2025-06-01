package com.fiap.itmoura.tech_challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {

    ADMIN("ADMIN"),
    DELIVERY_DRIVER("DELIVERY_DRIVER"),
    CLIENT("CLIENT");

    private final String role;

}
