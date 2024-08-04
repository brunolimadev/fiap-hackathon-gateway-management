package br.com.fiap.gatewaymanagement.infra.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

@RestController
@RequestMapping("/autenticacao")
@Tag(name = "Auth Controller", description = "User authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final GenerateJwtInteractor generateJwtInteractor;

    public AuthController(AuthenticationManager authenticationManager, GenerateJwtInteractor generateJwtInteractor) {
        this.authenticationManager = authenticationManager;
        this.generateJwtInteractor = generateJwtInteractor;
    }

    @PostMapping
    public ResponseEntity<?> signIn(@Valid @RequestBody AuthDto credentials) throws Exception {

        // Cria instancia de autenticação com os dados do usuário
        var userCredentials = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());

        // Autentica o usuário
        var authentication = authenticationManager.authenticate(userCredentials);

        User user = GetUserByEmailMapper.toDomain((GetUserByEmailResponse) authentication.getPrincipal());

        // Gera o token
        var jwt = generateJwtInteractor.execute(user);

        // Adiciona o token no header da resposta
        HttpHeaders hearders = new HttpHeaders();

        // Adiciona a header Authorization com o token
        hearders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        // Cria o objeto de respota com o status e o nome do usuário
        SignInResponseDto response = new SignInResponseDto(true,
                ((GetUserByEmailResponse) authentication.getPrincipal()).getName());

        return ResponseEntity.ok().headers(hearders).body(response);
    }

}