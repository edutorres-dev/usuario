package com.javanauta.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*

Entidade JPA que representa a tabela "telefone" no banco de dados.

Cada objeto Telefone representa um telefone cadastrado no sistema,
contendo seu ID, DDD e número.

Essa entidade pode ser relacionada a um Usuario através do
relacionamento @OneToMany definido na classe Usuario.

*/

@Getter // Gera automaticamente os métodos getters.
@Setter // Gera automaticamente os métodos setters.
@AllArgsConstructor // Gera um construtor com todos os atributos.
@NoArgsConstructor // Gera um construtor vazio, necessário pelo JPA.

@Entity // Define a classe como uma entidade JPA.
@Table(name = "telefone") // Define "telefone" como nome da tabela
public class Telefone {

    @Id // Define o ID como chave primária.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o ID automaticamente pelo banco.
    private Long id;

    @Column(name = "numero", length = 100) // Define a coluna que armazena o número do telefone.
    private String numero;

    @Column(name = "ddd", length = 100) // Define a coluna que armazena o DDD.
    private String ddd;
}
