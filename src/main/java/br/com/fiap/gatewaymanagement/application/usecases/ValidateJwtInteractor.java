package br.com.fiap.gatewaymanagement.application.usecases;

import br.com.fiap.gatewaymanagement.application.gateways.JwtGateway;

public class ValidateJwtInteractor {

    private final JwtGateway jwtGateway;

    public ValidateJwtInteractor(JwtGateway jwtGateway) {
        this.jwtGateway = jwtGateway;
    }

    public String execute(String jwt) throws Exception {
        return jwtGateway.validateJwt(jwt);
    }

}
