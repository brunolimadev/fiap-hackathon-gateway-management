package br.com.fiap.gatewaymanagement.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import br.com.fiap.gatewaymanagement.application.usecases.GetUserByEmailInteractor;
import br.com.fiap.gatewaymanagement.application.usecases.ValidateJwtInteractor;
import br.com.fiap.gatewaymanagement.infra.controllers.exceptions.CustomAuthenticationFailureHandler;
import br.com.fiap.gatewaymanagement.main.filters.CustomGlobalFilter;

@Configuration
@EnableWebFluxSecurity
public class GatewayAppSecurityConfig {
    // private final SecurityFilter securityFilter;

    // public GatewayAppSecurityConfig(SecurityFilter securityFilter) {
    // this.securityFilter = securityFilter;
    // }

    private final GetUserByEmailInteractor getUserByEmailInteractor;

    private final ValidateJwtInteractor validateJwtInteractor;

    public GatewayAppSecurityConfig(GetUserByEmailInteractor getUserByEmailInteractor,
            ValidateJwtInteractor validateJwtInteractor) {
        this.getUserByEmailInteractor = getUserByEmailInteractor;
        this.validateJwtInteractor = validateJwtInteractor;
    }

    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {

        return http

                // Enable Basic Authentication with Default Configuration
                .httpBasic(Customizer.withDefaults())

                // Disable CSRF and Session Management to use Basic Authentication
                .csrf(csrf -> csrf.disable())

                .authorizeExchange(exchange -> exchange

                        // Permitir acesso às rotas de autenticação
                        .pathMatchers(HttpMethod.POST, "/gateway-management/api/autenticacao/**").permitAll()

                        // Permitir acesso às rotas de autenticação
                        .anyExchange().authenticated())
                .addFilterBefore(new CustomGlobalFilter(validateJwtInteractor, getUserByEmailInteractor),
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    /**
     * Método responsável por criar o AuthenticationManager
     *
     * @param userDetailsService
     * @param passwordEncoder
     * @return
     */
    @Bean
    ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(
                userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    /**
     * Método responsável por criar o PasswordEncoder
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}