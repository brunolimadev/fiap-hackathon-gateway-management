package br.com.fiap.gatewaymanagement.infra.models.dtos;

import jakarta.validation.constraints.Email;

public record AuthDto(
        @Email(message = "Deve ser um email válido.") String email,
        String password) {
}
