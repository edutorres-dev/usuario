package com.javanauta.usuario.business.controller;

import com.javanauta.usuario.infrastructure.exception.ConflictException;
import com.javanauta.usuario.infrastructure.exception.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Aplica o tratamento de exceções de forma global às Controllers.
@ControllerAdvice
public class GlobalExceptionHandler {

    // Trata exceções quando um recurso não é encontrado.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        // Retorna HTTP 404 com a mensagem da exceção.
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // Trata exceções quando ocorre conflito de dados.
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(
            ConflictException ex) {

        // Retorna HTTP 409 com a mensagem da exceção.
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }

    // Trata exceções quando o usuário não possui autorização.
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(
            UnauthorizedException ex) {

        // Retorna HTTP 401 com a mensagem da exceção.
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }
}
