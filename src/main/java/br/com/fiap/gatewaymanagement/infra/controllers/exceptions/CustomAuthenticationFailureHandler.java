package br.com.fiap.gatewaymanagement.infra.controllers.exceptions;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.gatewaymanagement.infra.models.dtos.errors.ErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        // Definição do status code da resposta 403 - Forbidden
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // Definição do tipo de conteúdo da resposta
        response.setContentType("application/json;charset=UTF-8");

        // Definição do tipo de conteúdo da resposta
        ErrorDto errorDto = new ErrorDto(
                "Credenciais inválidas",
                "Suas credenciais são inválidas para acessar este recurso.",
                String.valueOf(HttpStatus.FORBIDDEN.value()), null);

        // Escrita do JSON de resposta
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(errorDto));
        writer.flush();
    }

}
