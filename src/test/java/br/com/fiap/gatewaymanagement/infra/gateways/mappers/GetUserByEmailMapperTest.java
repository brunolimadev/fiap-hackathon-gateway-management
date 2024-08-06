package br.com.fiap.gatewaymanagement.infra.gateways.mappers;

import org.junit.jupiter.api.Test;

import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.domain.enums.UserRoleEnum;

import br.com.fiap.gatewaymanagement.infra.enums.RoleEnum;
import br.com.fiap.gatewaymanagement.infra.models.response.GetUserByEmailResponse;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;

class GetUserByEmailMapperTest {

    @Test
    void testToDto() {
        // Arrange
        User user = new User("teste", "test@test.com.br", "Teste@123", LocalDateTime.now(),
                UserRoleEnum.ROLE_USER);

        // Act
        GetUserByEmailResponse userDto = GetUserByEmailMapper.fromDomain(user);

        // Assert
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDto.isActive()).isEqualTo(user.isActive());
        assertThat(userDto.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(userDto.getRole()).isEqualTo(RoleEnum.ROLE_USER);
    }

    @Test
    void testToDomain() {
        // Arrange
        GetUserByEmailResponse userDto = new GetUserByEmailResponse(
                UUID.randomUUID(),
                "teste",
                "test@test.com.br",
                "Teste@123",
                true,
                LocalDateTime.now(),
                RoleEnum.ROLE_USER);

        // Act
        User user = GetUserByEmailMapper.toDomain(userDto);

        // Assert
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(user.isActive()).isEqualTo(userDto.isActive());
        assertThat(user.getCreatedAt()).isEqualTo(userDto.getCreatedAt());
        assertThat(user.getRole()).isEqualTo(UserRoleEnum.ROLE_USER);
    }
}