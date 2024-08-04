package br.com.fiap.gatewaymanagement.application.gateways;

import br.com.fiap.gatewaymanagement.domain.User;

public interface JwtGateway {

    String validateJwt(String jwt) throws Exception;

    String generateJwt(User user) throws Exception;

}
