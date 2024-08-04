package br.com.fiap.gatewaymanagement.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.gatewaymanagement.application.gateways.JwtGateway;
import br.com.fiap.gatewaymanagement.application.gateways.UserGateway;
import br.com.fiap.gatewaymanagement.application.usecases.GenerateJwtInteractor;
import br.com.fiap.gatewaymanagement.application.usecases.GetUserByEmailInteractor;
import br.com.fiap.gatewaymanagement.application.usecases.ValidateJwtInteractor;
import br.com.fiap.gatewaymanagement.infra.gateways.JwtRepositoryGateway;
import br.com.fiap.gatewaymanagement.infra.gateways.UserRepositoryGateway;

@Configuration
public class GatewayAppConfig {

    @Bean
    GetUserByEmailInteractor getUserByEmailInteractor(UserGateway userGateway) {
        return new GetUserByEmailInteractor(userGateway);
    }

    @Bean
    GenerateJwtInteractor generateJwtInteractor(JwtGateway jwtGateway) {
        return new GenerateJwtInteractor(jwtGateway);
    }

    @Bean
    ValidateJwtInteractor validateJwtInteractor(JwtGateway jwtGateway) {
        return new ValidateJwtInteractor(jwtGateway);
    }

    @Bean
    UserGateway userGateway(RestTemplate restTemplate) {
        return new UserRepositoryGateway(restTemplate);
    }

    @Bean
    JwtGateway jwtGateway() {
        return new JwtRepositoryGateway();
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }

}
