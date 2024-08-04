package br.com.fiap.gatewaymanagement.main.filters;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.gatewaymanagement.application.usecases.GetUserByEmailInteractor;
import br.com.fiap.gatewaymanagement.application.usecases.ValidateJwtInteractor;
import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.infra.gateways.mappers.GetUserByEmailMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final ValidateJwtInteractor validateJwtInteractor;

    private final GetUserByEmailInteractor getUserByEmailInteractor;

    public SecurityFilter(ValidateJwtInteractor validateJwtInteractor,
            GetUserByEmailInteractor getUserByEmailInteractor) {
        this.validateJwtInteractor = validateJwtInteractor;
        this.getUserByEmailInteractor = getUserByEmailInteractor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (this.getToken(request) != null) {

            var token = this.getToken(request);

            String email;
            User user;

            try {
                email = validateJwtInteractor.execute(token);
                user = getUserByEmailInteractor.execute(email);
            } catch (Exception e) {
                throw new IOException();
            }

            UserDetails userDetails = GetUserByEmailMapper.fromDomain(user);

            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        // Continua a requisição para o próximo filtro
        filterChain.doFilter(request, response);

    }

    /**
     * Método responsável por obter o token do header da requisição
     *
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            return authorization.split(" ")[1];
        }

        return null;
    }

}
