package br.com.fiap.gatewaymanagement.application.usecases;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.gatewaymanagement.application.gateways.JwtGateway;

@ExtendWith(MockitoExtension.class)
class ValidateJwtInteractorTest {

    private ValidateJwtInteractor validateJwtInteractor;

    @Mock
    private JwtGateway jwtGateway;

    @BeforeEach
    public void setup() {
        validateJwtInteractor = new ValidateJwtInteractor(jwtGateway);
    }

    @Test
    void testExecute() throws Exception {
        String jwt = "sample-jwt-token";
        String expected = "validated-jwt-token";

        when(jwtGateway.validateJwt(jwt)).thenReturn(expected);

        String result = validateJwtInteractor.execute(jwt);

        Assertions.assertEquals(expected, result);
    }

}