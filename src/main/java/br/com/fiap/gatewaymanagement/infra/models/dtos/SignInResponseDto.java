package br.com.fiap.gatewaymanagement.infra.models.dtos;

public record SignInResponseDto(
        boolean isAuthenticated,
        String name) {
}
