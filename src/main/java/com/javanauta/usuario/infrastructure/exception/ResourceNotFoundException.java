package com.javanauta.usuario.infrastructure.exception;

public class ResourceNotFoundException extends RuntimeException {

    /* Exceção personalizada usada quando um recurso solicitado
     não é encontrado na aplicação.

     Exemplos:
     - usuário não encontrado;
     - endereço não encontrado;
     - telefone não encontrado.

     RuntimeException permite lançar a exceção sem precisar
     declarar "throws" obrigatoriamente nos métodos.
     */

    // Construtor que recebe uma mensagem
    // informando // qual recurso não foi encontrado.
    public ResourceNotFoundException(String mensagem){
        // Envia a mensagem para a RuntimeException.
        super(mensagem);
    }

    // Construtor que recebe a mensagem e a exceção original
    // que causou o problema.
    public ResourceNotFoundException(String mensagem , Throwable trowable){
        // Envia a mensagem e a causa original para RuntimeException.
        super(mensagem,trowable);
    }


}
