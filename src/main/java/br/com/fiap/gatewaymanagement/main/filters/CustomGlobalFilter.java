package br.com.fiap.gatewaymanagement.main.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.google.gson.Gson;

import br.com.fiap.gatewaymanagement.application.usecases.GetUserByEmailInteractor;
import br.com.fiap.gatewaymanagement.application.usecases.ValidateJwtInteractor;
import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.infra.gateways.mappers.GetUserByEmailMapper;
import br.com.fiap.gatewaymanagement.infra.models.dtos.errors.ErrorDto;
import reactor.core.publisher.Mono;

public class CustomGlobalFilter implements WebFilter {

    private final ValidateJwtInteractor validateJwtInteractor;

    private final GetUserByEmailInteractor getUserByEmailInteractor;

    public CustomGlobalFilter(ValidateJwtInteractor validateJwtInteractor,
            GetUserByEmailInteractor getUserByEmailInteractor) {
        this.validateJwtInteractor = validateJwtInteractor;
        this.getUserByEmailInteractor = getUserByEmailInteractor;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (this.getToken(authHeader) != null) {

            var token = this.getToken(authHeader);

            String email;
            User user;

            try {
                email = validateJwtInteractor.execute(token);
                user = getUserByEmailInteractor.execute(email);
            } catch (Exception e) {
                return handleExpiredToken(exchange);
            }

            UserDetails userDetails = GetUserByEmailMapper.fromDomain(user);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())))
                    .onErrorResume(e -> handleError(exchange, e));

        }

        // Continua a requisição para o próximo filtro
        return chain.filter(exchange);
    }

    /**
     * Método responsável por obter o token do header da requisição
     *
     * @param request
     * @return
     */
    private String getToken(String authorization) {

        if (authorization != null && authorization.startsWith("Bearer")) {
            return authorization.split(" ")[1];
        }

        return null;
    }

    private Mono<Void> handleExpiredToken(ServerWebExchange exchange) {
        // Define a resposta para autenticação expirada
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");

        // Retorna uma mensagem de erro em formato JSON

        // Definição do tipo de conteúdo da resposta
        ErrorDto errorDto = new ErrorDto(
                "Credenciais inválidas",
                "Token expirado ou inválido.",
                String.valueOf(HttpStatus.UNAUTHORIZED.value()), null);

        return exchange.getResponse()
                .writeWith(
                        Mono.just(exchange.getResponse().bufferFactory().wrap(new Gson().toJson(errorDto).getBytes())));
    }

    private Mono<Void> handleError(ServerWebExchange exchange, Throwable e) {
        // Trata outros erros
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");

        // Retorna uma mensagem de erro genérica

        // Definição do tipo de conteúdo da resposta
        ErrorDto errorDto = new ErrorDto(
                "Ops...",
                "Recurso não autorizado.",
                String.valueOf(HttpStatus.UNAUTHORIZED.value()), null);

        return exchange.getResponse()
                .writeWith(
                        Mono.just(exchange.getResponse().bufferFactory().wrap(new Gson().toJson(errorDto).getBytes())));
    }

}
