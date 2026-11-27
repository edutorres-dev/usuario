package com.javanauta.usuario.infrastructure.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exceção para representar erros de acesso relacioandos a
 * à autenticação do usuário.
 */
public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String mensagem) {
        super(mensagem);
    }

    public UnauthorizedException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
