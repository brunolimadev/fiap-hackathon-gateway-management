package br.com.fiap.gatewaymanagement.application.usecases;

import br.com.fiap.gatewaymanagement.application.gateways.UserGateway;
import br.com.fiap.gatewaymanagement.domain.User;

public class GetUserByEmailInteractor {

    private final UserGateway userGateway;

    public GetUserByEmailInteractor(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(String email) throws Exception {
        return userGateway.getUserByEmail(email);
    }

}