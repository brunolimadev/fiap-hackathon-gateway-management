package br.com.fiap.gatewaymanagement.infra.gateways;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.fiap.gatewaymanagement.application.gateways.UserGateway;
import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.infra.gateways.mappers.GetUserByEmailMapper;
import br.com.fiap.gatewaymanagement.infra.models.response.GetUserByEmailResponse;
import reactor.core.publisher.Mono;

public class UserRepositoryGateway implements UserGateway, ReactiveUserDetailsService {

    private final WebClient.Builder webClientBuilder;

    @Value("${microservices.user-ms.url}")
    private String userMsUrl;

    public UserRepositoryGateway(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<User> getUserByEmail(String email) throws Exception {

        try {
            String url = new StringBuilder(userMsUrl)
                    .append("?byEmail=")
                    .append(email)
                    .toString();

            return webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(GetUserByEmailResponse.class)
                    .map(GetUserByEmailMapper::toDomain)
                    .onErrorMap(e -> new RuntimeException("Erro ao validar usuário!", e));

        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao validar usuário!");
        }
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        try {
            return getUserByEmail(username).map(GetUserByEmailMapper::fromDomain);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

}
