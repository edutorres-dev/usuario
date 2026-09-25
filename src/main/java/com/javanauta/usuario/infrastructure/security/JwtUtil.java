package com.javanauta.usuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/*
Classe responsável por criar, ler e validar tokens JWT.
 JWT é utilizado para identificar o usuário nas requisições
 depois que ele realiza o login.
 */
@Service
public class JwtUtil {

    // Chave secreta utilizada para assinar e validar o JWT.
    // IMPORTANTE: em produção, essa chave não deve ficar diretamente
    // no código. O ideal é utilizar uma variável de ambiente.
    private final String secretKey = "sua-chave-secreta-super-segura-que-deve-ser-bem-longa";

    // Gera um token JWT com o nome de usuário e validade de 1 hora
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Define o nome de usuário como o assunto do token
                .setIssuedAt(new Date()) // Define a data e hora de emissão do token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Define a data e hora de expiração (1 hora a partir da emissão)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256) // Converte a chave secreta em bytes e assina o token com ela
                .compact(); // Constrói o token JWT
    }

    //Claims são as informações/dados armazenados dentro de
    // um JWT sobre o usuário ou sobre o próprio token.

    // Extrai as claims do token JWT (informações adicionais do token)
    // Também verifica a assinatura do token usando a chave secreta.
    public Claims extractClaims(String token) {
        return Jwts.parser()
                // Informa a chave usada para verificar a assinatura do JWT.
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))) // Define a chave secreta para validar a assinatura do token
                .build() // Constrói o parser responsável por analisar o token.
                .parseClaimsJws(token) // Analisa o token JWT e obtém as claims
                .getBody(); // Retorna o corpo das claims (Retorna o conteúdo do token.)
    }

    // Extrai o nome de usuário do token JWT
    // Extrai o username armazenado no campo "subject" (sub) do JWT.
    public String extrairEmailToken(String token) {
        // Obtém o assunto (nome de usuário) das claims do token
        return extractClaims(token).getSubject();
    }

    // Verifica se o token JWT está expirado
    public boolean isTokenExpired(String token) {
        // Compara a data de expiração do token com a data atual
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Valida o token JWT verificando o nome de usuário e se o token não está expirado
    public boolean validateToken(String token, String username) {
        // Extrai o nome de usuário do token
        final String extractedUsername = extrairEmailToken(token);
        // Verifica se o nome de usuário do token corresponde ao fornecido e se o token não está expirado
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
