package br.com.fiap.gatewaymanagement.application.usecases;

import br.com.fiap.gatewaymanagement.application.gateways.JwtGateway;

public class GenerateJwtInteractor {

    private final JwtGateway jwtGateway;

    public GenerateJwtInteractor(JwtGateway jwtGateway) {
        this.jwtGateway = jwtGateway;
    }

    public String execute(Object user) throws Exception {
        return jwtGateway.generateJwt(user);
    }
}
