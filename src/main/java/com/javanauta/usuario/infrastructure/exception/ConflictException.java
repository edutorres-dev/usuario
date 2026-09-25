package com.javanauta.usuario.infrastructure.exception;



// Exceção personalizada utilizada para representar conflitos na aplicação.
//
// Por exemplo:
// - tentar cadastrar um email que já existe;
// - tentar criar um recurso que entra em conflito com outro.
//
// RuntimeException permite que a exceção seja lançada sem a necessidade
// de declarar "throws" obrigatoriamente nos métodos.

public class ConflictException extends RuntimeException {

    // Construtor que recebe uma mensagem explicando o motivo do conflito.
    public ConflictException(String mensagem){
        // Envia a mensagem para a classe RuntimeException.
        super(mensagem);
    }
    // Construtor que permite receber uma mensagem e uma exceção original.
    public ConflictException(String mensagem , Throwable throwable){
        // Passa a mensagem e a causa original para RuntimeException.
        super(mensagem);
    }

}
