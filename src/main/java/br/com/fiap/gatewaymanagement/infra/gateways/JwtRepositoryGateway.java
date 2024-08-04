package br.com.fiap.gatewaymanagement.infra.gateways;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fiap.gatewaymanagement.application.gateways.JwtGateway;
import br.com.fiap.gatewaymanagement.domain.User;

public class JwtRepositoryGateway implements JwtGateway {

    private static final String ISSUER = "FIAP Hackathon Payment System";

    @Value("${api.security.jwt.secret}")
    private String secret;

    @Override
    public String validateJwt(String jwt) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(jwt)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Erro ao validar token!", e);

        }
    }

    @Override
    public String generateJwt(User user) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId().toString())
                    .withClaim("role", user.getRole().getRole())
                    .withExpiresAt(generateExpirationDate(2))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token!", e);
        }
    }

    /**
     * Método responsável por gerar a data de expiração do token
     *
     * @return
     */
    public static Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

    /**
     * Método responsável por gerar a data de expiração do token
     * 
     * @param minutes horas para expiração
     *
     * @return
     */
    public static Instant generateExpirationDate(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.of("-03:00"));
    }

}
