package com.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.blog.entities.User;
import com.blog.exceptions.TokenValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secretKey;

    private static final String ISSUER = "Blog-api";

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withIssuedAt(generateIssuedTime())
                    .withExpiresAt(generateExpirationTime())
                    .sign(algorithm);
        } catch (IllegalArgumentException | JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new TokenValidationException("Token inválido ou expirado.", exception);
        }
    }

    private Instant generateIssuedTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
    }

    private Instant generateExpirationTime() {
        return LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant();
    }
}
