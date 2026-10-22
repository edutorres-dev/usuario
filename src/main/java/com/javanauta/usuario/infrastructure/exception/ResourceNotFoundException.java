package com.javanauta.usuario.infrastructure.exception;

public class ResourceNotFoundException extends RuntimeException {

    /* Exceção usada quando um recurso solicitado
     não é encontrado na aplicação ( usuário/endereço/telefone por exemplo)
     */


    public ResourceNotFoundException(String mensagem){
        super(mensagem);
    }


    public ResourceNotFoundException(String mensagem , Throwable trowable){
        super(mensagem,trowable);
    }


}
