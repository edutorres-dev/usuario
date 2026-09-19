package com.javanauta.usuario.infrastructure.repository;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


/* Repository responsável por acessar e manipular os dados de
 Usuario no banco. O JpaRepository do Spring Data JPA
 já fornece métodos de CRUD automaticamente sem necessidade
 de declarar
 */

public interface UsuarioRepository extends JpaRepository <Usuario,Long> {

    // verifica o email no banco e retorna true ( se existe ) ou false(se não existe)
    boolean existsByEmail(String email);

    // optional classe o java Util serve para evitar o retorno de informações nulas
    // em vez de usar o NullPointerException quebrando o programa
    // usamos o option para que trate melhor
    Optional<Usuario> findByEmail( String email);

    // Permite executar a operação de exclusão dentro de uma transação.
    @Transactional
    void deleteByEmail(String email);
}
