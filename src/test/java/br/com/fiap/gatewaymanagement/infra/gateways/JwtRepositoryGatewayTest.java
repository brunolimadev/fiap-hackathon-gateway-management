package br.com.fiap.gatewaymanagement.infra.gateways;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.domain.enums.UserRoleEnum;

@ExtendWith(MockitoExtension.class)
class JwtRepositoryGatewayTest {

    private JwtRepositoryGateway jwtRepositoryGateway;

    @BeforeEach
    void setup() {
        jwtRepositoryGateway = new JwtRepositoryGateway();
        ReflectionTestUtils.setField(jwtRepositoryGateway, "secret", "fiaphackathonpaymentsystem");
    }

    @Test
    void testValidateJwt() throws Exception {
        String email = "mail@mail.com.br";

        User user = new User(
                UUID.randomUUID(),
                "teste",
                email,
                "Teste@123",
                true,
                LocalDateTime.now(),
                UserRoleEnum.ROLE_ADMIN);

        String token = jwtRepositoryGateway.generateJwt(user);

        String result = jwtRepositoryGateway.validateJwt(token);

        assertThat(result).isEqualTo(email);
    }

    @Test
    void testValidateJwtWithError() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJGSUFQIEhhY2thdGhvbiBQYXltZW50IFN5c3RlbSIsInN1YiI6ImFkajJAZW1haWwuY29tIiwiaWQiOiIxNjUxM2FjMS1hZjNlLTQ1YjUtYTM1NS04MzczN2ExZTA3NjIiLCJyb2xlIjoiQURNSU4iLCJleHAiOjE3MjI4MDYyNDN9.htMKO7AmRSxfFHv0XgleTWQqhLE5DiW0Bc2nCbjSjTg";

        assertThrows(RuntimeException.class, () -> jwtRepositoryGateway.validateJwt(jwt));
    }

}