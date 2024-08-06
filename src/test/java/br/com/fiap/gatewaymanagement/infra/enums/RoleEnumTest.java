package br.com.fiap.gatewaymanagement.infra.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleEnumTest {

    @Test
    void testGetRole_AdminRole_ReturnsAdmin() {
        // Arrange
        RoleEnum role = RoleEnum.ROLE_ADMIN;

        // Act
        String result = role.getRole();

        // Assert
        assertThat(result).isEqualTo("ADMIN");
    }

    @Test
    void testGetRole_UserRole_ReturnsUser() {
        // Arrange
        RoleEnum role = RoleEnum.ROLE_USER;

        // Act
        String result = role.getRole();

        // Assert
        assertThat(result).isEqualTo("USER");
    }
}
