
package com.javanauta.usuario.business.dto;

import lombok.*;
/*
 * ==========================================================
 * DTO - DATA TRANSFER OBJECT
 * ==========================================================
 *
 * O EnderecoDTO é utilizado para transportar os dados de um
 * endereço entre as diferentes camadas da aplicação.
 *
 * Diferente da Entity, o DTO não representa diretamente uma
 * tabela do banco de dados.
 *
 * Nesse projeto, podemos utilizar o DTO para receber os dados
 * enviados pelo cliente através da API e também para devolver
 * informações na resposta.
 *
 * Fluxo:
 *
 * JSON
 *   ↓
 * EnderecoDTO
 *   ↓
 * Converter
 *   ↓
 * Endereco (Entity)
 *   ↓
 * Banco de dados
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnderecoDTO {

    private Long id;
    private String rua;
    private Long numero;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
}

