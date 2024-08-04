package br.com.fiap.gatewaymanagement.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.gatewaymanagement.domain.enums.UserRoleEnum;

public class User {

    private UUID id;

    private String name;

    private String email;

    private String password;

    private boolean isActive;

    private LocalDateTime createdAt;

    private UserRoleEnum role;

    public User(
            UUID id,
            String name,
            String email,
            String password,
            boolean isActive,
            LocalDateTime createdAt,
            UserRoleEnum role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.role = role;
    }

    public User(
            String name,
            String email,
            String password,
            LocalDateTime createdAt,
            UserRoleEnum role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserRoleEnum getRole() {
        return role;
    }

}
