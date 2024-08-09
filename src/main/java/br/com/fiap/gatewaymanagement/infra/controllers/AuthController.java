package br.com.fiap.gatewaymanagement.infra.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.gatewaymanagement.application.usecases.GenerateJwtInteractor;
import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.infra.gateways.mappers.GetUserByEmailMapper;
import br.com.fiap.gatewaymanagement.infra.models.dtos.AuthDto;
import br.com.fiap.gatewaymanagement.infra.models.dtos.SignInResponseDto;
import br.com.fiap.gatewaymanagement.infra.models.response.GetUserByEmailResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@Tag(name = "Auth Controller", description = "User authentication")
public class AuthController {

    private final ReactiveAuthenticationManager authenticationManager;
    private final GenerateJwtInteractor generateJwtInteractor;

    public AuthController(
            ReactiveAuthenticationManager authenticationManager, GenerateJwtInteractor generateJwtInteractor) {
        this.authenticationManager = authenticationManager;
        this.generateJwtInteractor = generateJwtInteractor;
    }

    @PostMapping("/autenticacao")
    public Mono<ResponseEntity<SignInResponseDto>> signIn(@Valid @RequestBody AuthDto credentials) {

        // Cria instancia de autenticação com os dados do usuário
        var userCredentials = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());

        // Autentica o usuário
        return authenticationManager.authenticate(userCredentials).flatMap(authentication -> {

            User user = GetUserByEmailMapper.toDomain((GetUserByEmailResponse) authentication.getPrincipal());

            // Gera o token
            String jwt;
            try {
                jwt = generateJwtInteractor.execute(user);

                String bearer = String.format("Bearer %s", jwt);

                // Cria o objeto de respota com o status e o nome do usuário
                SignInResponseDto response = new SignInResponseDto(bearer);

                // Retorna a resposta com o token no header
                return Mono.just(ResponseEntity.ok().body(response));
            } catch (Exception e) {
                throw new RuntimeException("Erro ao gerar token");
            }

        }).onErrorResume(Exception.class, e -> Mono.just(ResponseEntity.badRequest().build()));
        // return ResponseEntity.ok().headers(hearders).body(response);
    }

}