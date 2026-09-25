
package com.javanauta.usuario.business.converter;
import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioConverter {

    /*
     * ==========================================================
     * CONVERSÃO DE DTO PARA ENTITY
     * ==========================================================
     *
     * O método abaixo recebe um UsuarioDTO e transforma seus dados em um objeto Usuario (Entity).

     * ----------------------------------------------------------
     * CONVERSÃO SEM BUILDER
     * ----------------------------------------------------------
     *
     * Uma forma tradicional seria criar o objeto com "new" e
     * preencher cada atributo utilizando os setters:
     *
     * Usuario usuario = new Usuario();
     * usuario.setNome(usuarioDTO.getNome());
     * usuario.setEmail(usuarioDTO.getEmail());
     * usuario.setSenha(usuarioDTO.getSenha());
     *
     * ----------------------------------------------------------
     * CONVERSÃO COM BUILDER
     * ----------------------------------------------------------
     *
     * Utilizando o padrão Builder, conseguimos criar o objeto
     * de forma mais organizada e legível.
     *
     * O Builder normalmente é gerado pelo Lombok através da
     * anotação @Builder na Entity.
     */
    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {

        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefones(usuarioDTO.getTelefones()))
                .build();
    }


    /*
     * ==========================================================
     * CONVERSÃO DE LISTA DE ENDEREÇOS
     * ==========================================================
     *
     * Recebe uma lista de EnderecoDTO e transforma cada elemento
     * em um objeto Endereco.
     *
     * EnderecoDTO -> Endereco
     *
     * Para realizar a conversão de cada elemento utilizamos:
     *
     * stream() -> transforma a lista em um Stream.
     *
     * map() -> aplica uma operação em cada elemento da lista.
     *
     * this::paraEndereco ->  indica que cada EnderecoDTO deve ser enviado para o método paraEndereco().
     *
     * toList() -> transforma o resultado novamente em uma lista.
     *
     */
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS) {

        // Utilizando Stream + Map para converter cada elemento
        // da lista de EnderecoDTO para Endereco.
        return enderecoDTOS.stream().map(this::paraEndereco).toList();

        /*
         * Outra possibilidade seria utilizar um laço for:
         *
         * List<Endereco> enderecos = new ArrayList<>();
         *
         * for (EnderecoDTO enderecoDTO : enderecoDTOS) {
         *     enderecos.add(paraEndereco(enderecoDTO));
         * }
         *
         * return enderecos;
         */
    }


    /*
     * ==========================================================
     * CONVERSÃO DE UM ENDEREÇO
     * ==========================================================
     *
     * Converte um único EnderecoDTO para uma Entity Endereco.
     *
     * EnderecoDTO -> Endereco
     */
    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {

        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .estado(enderecoDTO.getEstado())
                .cep(enderecoDTO.getCep())
                .build();
    }


    /*
     * ==========================================================
     * CONVERSÃO DE LISTA DE TELEFONES
     * ==========================================================
     */
    public List<Telefone> paraListaTelefones(List<TelefoneDTO> telefoneDTOS) {


        return telefoneDTOS.stream().map(this::paraTelefone).toList();

        /*
         * Também seria possível utilizar um laço for:
         *
         * List<Telefone> telefones = new ArrayList<>();
         *
         * for (TelefoneDTO telefoneDTO : telefoneDTOS) {
         *     telefones.add(paraTelefone(telefoneDTO));
         * }
         *
         * return telefones;
         */
    }


    /*
     * ==========================================================
     * CONVERSÃO DE UM TELEFONE
     * ==========================================================
     *
     * Converte um único TelefoneDTO para uma Entity Telefone.
     *
     * TelefoneDTO -> Telefone
     */
    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {

        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }


    /*
     * ==========================================================
     * CONVERSÃO DE ENTITY PARA DTO
     * ==========================================================
     *
     * Agora fazemos o caminho inverso:
     *
     * Usuario -> UsuarioDTO
     *
     * Essa conversão é muito utilizada quando a aplicação busca
     * informações no banco de dados e precisa devolver esses
     * dados para o cliente através de uma API REST.
     *
     * Exemplo:
     *
     * Banco de dados
     *       ↓
     *     Entity
     *       ↓
     *   Converter
     *       ↓
     *      DTO
     *       ↓
     * Resposta da API
     */
    public UsuarioDTO paraUsuarioDTO(Usuario usuario) {

        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(paraListaEnderecoDTO(usuario.getEnderecos()))
                .telefones(paraListaTelefonesDTO(usuario.getTelefones()))
                .build();
    }


    /*
     * ==========================================================
     * CONVERSÃO DE LISTA DE ENDEREÇOS PARA DTO
     * ==========================================================
     *
     * Recebe uma lista de Endereco e transforma cada elemento
     * em EnderecoDTO.
     *
     * Endereco -> EnderecoDTO
     *
     * Aqui estamos utilizando um laço for para demonstrar outra
     * maneira de realizar a conversão.
     */
    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecos) {

        // Criamos uma lista vazia que receberá os EnderecoDTO.
        List<EnderecoDTO> enderecosDTO = new ArrayList<>();

        // Percorremos cada Endereco existente na lista.
        for (Endereco endereco : enderecos) {

            // Convertemos o Endereco para EnderecoDTO e adicionamos
            // o resultado na nova lista.
            enderecosDTO.add(paraEnderecoDTO(endereco));
        }

        return enderecosDTO;

        /*
         * Também poderíamos fazer utilizando Stream + Map:
         *
         * return enderecos.stream()
         *         .map(this::paraEnderecoDTO)
         *         .toList();
         */
    }


    /*
     * ==========================================================
     * CONVERSÃO DE UM ENDEREÇO PARA DTO
     * ==========================================================
     *
     * Converte uma Entity Endereco para EnderecoDTO.
     *
     * Endereco -> EnderecoDTO
     */
    public EnderecoDTO paraEnderecoDTO(Endereco endereco) {

        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }


    /*
     * ==========================================================
     * CONVERSÃO DE LISTA DE TELEFONES PARA DTO
     * ==========================================================
     *
     * Recebe uma lista de Telefone e transforma cada elemento
     * em TelefoneDTO.
     *
     * Telefone -> TelefoneDTO
     */
    public List<TelefoneDTO> paraListaTelefonesDTO(List<Telefone> telefones) {

        // Utilizamos Stream + Map para converter cada telefone.
        return telefones.stream()
                .map(this::paraTelefoneDTO)
                .toList();

        /*
         * Outra possibilidade seria utilizar um laço for:
         *
         * List<TelefoneDTO> telefonesDTO = new ArrayList<>();
         *
         * for (Telefone telefone : telefones) {
         *     telefonesDTO.add(paraTelefoneDTO(telefone));
         * }
         *
         * return telefonesDTO;
         */
    }


    /*
     * ==========================================================
     * CONVERSÃO DE UM TELEFONE PARA DTO
     * ==========================================================
     *
     * Converte uma Entity Telefone para TelefoneDTO.
     *
     * Telefone -> TelefoneDTO
     */
    public TelefoneDTO paraTelefoneDTO(Telefone telefone) {

        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }


    /*
     ========================
     ATUALIZAR DADOS DO USUÁRIO
     =========================
    */
    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity) {

        return Usuario.builder()

                // Se o DTO tiver nome, atualiza; caso contrário, mantém o nome atual.
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : entity.getNome())

                // O ID não é alterado; mantém o ID original da Entity.
                .id(entity.getId())

                // Se houver nova senha, atualiza; caso contrário, mantém a senha atual.
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : entity.getSenha())

                // Se houver novo e-mail, atualiza; caso contrário, mantém o e-mail atual.
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : entity.getEmail())

                // Mantém os endereços já cadastrados para o usuário.
                .enderecos(entity.getEnderecos())

                // Mantém os telefones já cadastrados para o usuário.
                .telefones(entity.getTelefones())

                .build();
    }


    public Endereco updateEndereco(EnderecoDTO dto , Endereco entity){
        return  Endereco.builder()
                .id(entity.getId())
                .rua(dto.getRua() != null ? dto.getRua() : entity.getRua())
                .numero(dto.getNumero() != null ?  dto.getNumero() : entity.getNumero())
                .cidade(dto.getCidade() != null ? dto.getCidade() : entity.getCidade())
                .cep(dto.getCep() != null ? dto.getCep() : entity.getCep())
                .complemento(dto.getComplemento()!= null ? dto.getComplemento() : entity.getComplemento())
                .estado(dto.getEstado() != null ?  dto.getEstado() : entity.getEstado())
                .build();

    }

    public Telefone updateTelefone(TelefoneDTO dto ,Telefone entity ){
        return Telefone.builder()
                .id(entity.getId())
                .ddd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .build();
    }

    // converntendo de dto para entity
    public Endereco paraEnderecoEntity(EnderecoDTO dto,Long idUsuario){
        return Endereco.builder()
                .rua(dto.getRua())
                .cidade(dto.getCidade())
                .cep(dto.getCep())
                .complemento(dto.getComplemento())
                .estado(dto.getEstado())
                .numero(dto.getNumero())
                .usuario_id(idUsuario)
                .build();
    }

    public Telefone paraTelefoneEntity(TelefoneDTO dto, Long idUsuario){
        return Telefone.builder()
                .numero(dto.getNumero())
                .ddd(dto.getDdd())
                .usuario_id(idUsuario)
                .build();
    }


}
