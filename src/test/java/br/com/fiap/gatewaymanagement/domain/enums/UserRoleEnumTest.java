package br.com.fiap.gatewaymanagement.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleEnumTest {

    @Test
    void testGetRole_AdminRole_ReturnsAdmin() {
        // Arrange
        UserRoleEnum role = UserRoleEnum.ROLE_ADMIN;

        // Act
        String result = role.getRole();

        // Assert
        assertThat(result).isEqualTo("ADMIN");
    }

    @Test
    void testGetRole_UserRole_ReturnsUser() {
        // Arrange
        UserRoleEnum role = UserRoleEnum.ROLE_USER;

        // Act
        String result = role.getRole();

        // Assert
        assertThat(result).isEqualTo("USER");
    }
}
