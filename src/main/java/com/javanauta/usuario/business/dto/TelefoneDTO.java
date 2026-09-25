package com.javanauta.usuario.business.dto;

import lombok.*;

/*
 * ==========================================================
 * DTO - DATA TRANSFER OBJECT
 * ==========================================================
 *
 * O TelefoneDTO é utilizado para transportar os dados de um
 * telefone entre as diferentes camadas da aplicação.
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
 * TelefoneDTO
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

public class TelefoneDTO {
    private Long id;
    private String numero;
    private String ddd;
}
