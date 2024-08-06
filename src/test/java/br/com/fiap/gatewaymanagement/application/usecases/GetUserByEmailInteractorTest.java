package br.com.fiap.gatewaymanagement.application.usecases;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.gatewaymanagement.application.gateways.UserGateway;
import br.com.fiap.gatewaymanagement.application.usecases.GetUserByEmailInteractor;
import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.domain.enums.UserRoleEnum;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GetUserByEmailInteractorTest {

    private GetUserByEmailInteractor getUserByEmailInteractor;

    @Mock
    private UserGateway userGateway;

    @BeforeEach
    public void setup() {
        getUserByEmailInteractor = new GetUserByEmailInteractor(userGateway);
    }

    @Test
    void testGetUserByEmail() throws Exception {
        String email = "mail@mail.com.br";

        User user = new User("teste", "mail@mail.com.br", "Teste@123", LocalDateTime.now(),
                UserRoleEnum.ROLE_USER);

        when(userGateway.getUserByEmail(email)).thenReturn(Mono.just(user));

        getUserByEmailInteractor.execute(email);

        Assertions.assertThat(email).isEqualTo(user.getEmail());
    }

}
