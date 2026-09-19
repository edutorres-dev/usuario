package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    /*
     * ==========================================================
     * DEPENDÊNCIAS
     * ==========================================================
     *
     * O Service é responsável por concentrar as regras de negócio
     * da aplicação.
     *
     * Neste caso, precisamos de duas dependências:
     *
     * UsuarioRepository:
     * - Responsável por realizar a comunicação com o banco de
     *   dados através do Spring Data JPA.
     *
     * UsuarioConverter:
     * - Responsável por realizar a conversão entre DTO e Entity.
     *
     * O @RequiredArgsConstructor do Lombok cria automaticamente
     * um construtor com esses atributos final, permitindo que o
     * Spring faça a injeção de dependência.
     */
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;


    /*
     * ==========================================================
     * SALVAR USUÁRIO
     * ==========================================================
     *
     * Esse método recebe um UsuarioDTO vindo do Controller.
     *
     * O fluxo será:
     *
     * UsuarioDTO
     *     ↓
     * Converter
     *     ↓
     * Usuario (Entity)
     *     ↓
     * Repository
     *     ↓
     * Banco de dados
     *     ↓
     * Usuario (Entity)
     *     ↓
     * Converter
     *     ↓
     * UsuarioDTO
     *
     * O DTO é utilizado para transportar os dados entre as
     * camadas da aplicação, enquanto a Entity representa o objeto
     * que será persistido no banco de dados.
     */
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {

        /*
         * ======================================================
         * 1 - CONVERSÃO DE DTO PARA ENTITY
         * ======================================================
         *
         * O Controller recebeu um UsuarioDTO.
         * Porém, o Repository trabalha com a Entity Usuario.
         * Por isso, utilizamos o UsuarioConverter para transformar:
         *
         * UsuarioDTO -> Usuario
         */
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);


        /*
         * ======================================================
         * 2 - SALVANDO A ENTITY NO BANCO
         * ======================================================
         *
         * O método save() do UsuarioRepository é responsável por
         * salvar a Entity no banco de dados.
         *
         * O objeto retornado pelo save() é atribuído novamente
         * à variável usuario.
         */
        usuario = usuarioRepository.save(usuario);


        /*
         * ======================================================
         * 3 - CONVERSÃO DE ENTITY PARA DTO
         * ======================================================
         *
         * Depois que o usuário foi salvo, temos uma Entity:
         *
         * Usuario
         *
         * Porém, o método precisa retornar um:
         *
         * UsuarioDTO
         *
         * Por isso, fazemos novamente a conversão:
         *
         * Usuario -> UsuarioDTO
         *
         * Dessa forma, o Controller poderá devolver o DTO como
         * resposta da API.
         */
        return usuarioConverter.paraUsuarioDTO(usuario);
    }
}
