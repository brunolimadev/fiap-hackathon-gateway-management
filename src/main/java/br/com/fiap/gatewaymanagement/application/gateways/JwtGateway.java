package br.com.fiap.gatewaymanagement.application.gateways;

public interface JwtGateway {

    String validateJwt(String jwt) throws Exception;

    String generateJwt(Object user) throws Exception;

}
