package br.com.fiap.gatewaymanagement.infra.models.dtos.errors;

public record ErrorFieldDto(
        String field,
        String message) {
}
