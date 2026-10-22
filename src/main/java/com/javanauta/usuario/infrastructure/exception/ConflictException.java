package com.javanauta.usuario.infrastructure.exception;


/*

Exceção para representar conflitos na aplicação
( tentar cadastrar um email já existente por exemplo)
 tentar cadastrar um email que já existe;

*/
public class ConflictException extends RuntimeException {

    public ConflictException(String mensagem){
        super(mensagem);
    }

    public ConflictException(String mensagem , Throwable throwable){
        super(mensagem);
    }

}
