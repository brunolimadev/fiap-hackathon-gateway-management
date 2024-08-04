package br.com.fiap.gatewaymanagement.application.gateways;

import br.com.fiap.gatewaymanagement.domain.User;
import reactor.core.publisher.Mono;

public interface UserGateway {

    Mono<User> getUserByEmail(String email) throws Exception;

}
