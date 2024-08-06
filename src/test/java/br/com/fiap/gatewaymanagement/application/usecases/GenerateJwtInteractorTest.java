package br.com.fiap.gatewaymanagement.application.usecases;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.gatewaymanagement.application.gateways.JwtGateway;
import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.domain.enums.UserRoleEnum;

@ExtendWith(MockitoExtension.class)
class GenerateJwtInteractorTest {

    private GenerateJwtInteractor generateJwtInteractor;

    @Mock
    private JwtGateway jwtGateway;

    @BeforeEach
    public void setup() {
        generateJwtInteractor = new GenerateJwtInteractor(jwtGateway);
    }

    @Test
    void testGenerateJwt() throws Exception {
        User user = new User("teste", "mail@mail.com.br", "Teste@123", LocalDateTime.now(),
                UserRoleEnum.ROLE_USER);

        String jwt = "generated-jwt-token";

        when(jwtGateway.generateJwt(user)).thenReturn(jwt);

        String result = generateJwtInteractor.execute(user);

        Assertions.assertEquals(jwt, result);
    }

}