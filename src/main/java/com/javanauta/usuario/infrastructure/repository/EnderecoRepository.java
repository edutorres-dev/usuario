package com.javanauta.usuario.infrastructure.repository;

import com.javanautaaprendendo_spring.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

/*
Repository é a classe responsável por acessar e manipular os dados
de Endereco no banco. Ao herdar de JpaRepository, o Spring Data JPA
fornece automaticamente operações de CRUD ( não é necessário criá-los)
*/

// JpaRepository<Endereco, Long> onde
// Endereco → entidade que o repository irá gerenciar
// e Long tipo do ID da entidade Endereco
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {
}
