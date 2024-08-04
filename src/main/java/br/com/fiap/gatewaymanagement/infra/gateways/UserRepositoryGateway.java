package br.com.fiap.gatewaymanagement.infra.gateways;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.gatewaymanagement.application.gateways.UserGateway;
import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.infra.gateways.mappers.GetUserByEmailMapper;
import br.com.fiap.gatewaymanagement.infra.models.response.GetUserByEmailResponse;

public class UserRepositoryGateway implements UserGateway, UserDetailsService {

    private final RestTemplate restTemplate;

    @Value("${microservices.user-ms.url}")
    private String userMsUrl;

    public UserRepositoryGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            return GetUserByEmailMapper.fromDomain(getUserByEmail(username));
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

    @Override
    public User getUserByEmail(String email) throws Exception {

        try {
            String url = new StringBuilder(userMsUrl)
                    .append("?byEmail=")
                    .append(email)
                    .toString();

            ResponseEntity<GetUserByEmailResponse> response = this.restTemplate.getForEntity(url,
                    GetUserByEmailResponse.class);

            return GetUserByEmailMapper.toDomain(response.getBody());

        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao validar usuário!");
        }
    }

}
