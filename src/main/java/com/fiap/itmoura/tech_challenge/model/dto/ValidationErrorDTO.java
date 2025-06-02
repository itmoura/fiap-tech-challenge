package com.fiap.itmoura.tech_challenge.model.dto;

import java.util.List;

public record ValidationErrorDTO(List<String> errors, int status) {
}
