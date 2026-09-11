package com.javanauta.usuario.infrastructure.repository;

import com.javanautaaprendendo_spring.infrastructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 Repository responsável por acessar e manipular os dados de Telefone no banco.
 Ao herdar de JpaRepository, o Spring Data JPA fornece automaticamente
 métodos de CRUD
 JpaRepository<Telefone, Long> significa:
 Telefone → entidade que o repository irá gerenciar.
 Long → tipo do ID da entidade Telefone.
 */
public interface TelefoneRepository extends JpaRepository<Telefone,Long> {
}
