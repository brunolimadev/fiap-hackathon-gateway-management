package br.com.fiap.gatewaymanagement.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.fiap.gatewaymanagement.infra.controllers.exceptions.CustomAuthenticationFailureHandler;
import br.com.fiap.gatewaymanagement.main.filters.SecurityFilter;

@Configuration
@EnableWebSecurity
public class GatewayAppSecurityConfig {
    private final SecurityFilter securityFilter;

    public GatewayAppSecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http

                // Enable Basic Authentication with Default Configuration
                .httpBasic(Customizer.withDefaults())

                // Disable CSRF and Session Management to use Basic Authentication
                .csrf(csrf -> csrf.disable())

                // Disable Session Management to use Basic Authentication
                .sessionManagement(AbstractHttpConfigurer::disable)

                // Authorize Requests
                .authorizeHttpRequests(
                        authorize -> authorize

                                // Allow access to Swagger
                                .requestMatchers("/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()

                                // Allow access to application
                                .requestMatchers(HttpMethod.POST, "/auth/signin").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()

                                // Any other request must be authenticated
                                .requestMatchers(HttpMethod.GET, "/auth/signout").authenticated()
                                .anyRequest().authenticated())
                .addFilterBefore(securityFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationFailureHandler()::onAuthenticationFailure))
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
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
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
