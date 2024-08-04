package br.com.fiap.gatewaymanagement.infra.enums;

public enum RoleEnum {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}