package br.com.fiap.gatewaymanagement.domain.enums;

public enum UserRoleEnum {

    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String role;

    UserRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
