package br.com.fiap.gatewaymanagement.application.gateways;

import br.com.fiap.gatewaymanagement.domain.User;

public interface UserGateway {

    User getUserByEmail(String email) throws Exception;

}
