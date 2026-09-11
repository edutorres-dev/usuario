package com.javanauta.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuario")

/*

UserDetails é uma interface do Spring Security que representa
os dados de um usuário que será autenticado no sistema.

Implementamos UserDetails na classe Usuario para informar ao
Spring Security quais são os dados usados na autenticação:

 getUsername()    → identifica o usuário (neste projeto, o email)
 getPassword()    → retorna a senha/hash do usuário
 getAuthorities() → retorna as roles/permissões do usuário

Ao implementar UserDetails, a classe Usuario passa a ser reconhecida
pelo Spring Security como um usuário que pode ser autenticado .
*/

public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // gera automaticamente os ids
    private Long id;
    @Column(name = "nome",length = 100)
    private String nome;
    @Column(name = "email",length = 100)
    private String email;
    @Column(name = "senha")
    private String senha;
    // Um usuário pode possuir vários endereços.
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="usuario_id",referencedColumnName = "id")
    private List<Endereco> enderecos;
    @OneToMany(cascade = CascadeType.ALL)
    // Um usuário pode possuir vários telefones.
    @JoinColumn(name="usuario_id",referencedColumnName = "id")
    private List<Telefone> telefones;

    // Define qual informação será usada como "username".
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, o usuário não possui nenhuma role( nivel de permissão
        // do usuário dentro do sistema).
        return List.of();
    }

    // Retorna a senha usada pelo Spring Security.
    @Override
    public @Nullable String getPassword() {
        return senha ;
    }

    // Define qual informação será usada como "username".
    @Override
    // Neste projeto, o email é usado como username.
    public String getUsername() {
        return email;
    }
}
