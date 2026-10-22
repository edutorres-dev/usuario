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

    // Converte um UsuarioDTO para a Entity Usuario.
    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {

        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(usuarioDTO.getEnderecos() != null ?
                        paraListaEndereco(usuarioDTO.getEnderecos()) : null)
                .telefones(usuarioDTO.getTelefones() != null ?
                        paraListaTelefones(usuarioDTO.getTelefones()) : null)
                .build();
    }


    // Converte uma lista de EnderecoDTO para uma lista de Endereco.
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS) {

        // Utiliza Stream + Map para converter cada endereço da lista.
        return enderecoDTOS.stream()
                .map(this::paraEndereco)
                .toList();
    }


    // Converte um EnderecoDTO para uma Entity Endereco.
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


    // Converte uma lista de TelefoneDTO para uma lista de Telefone.
    public List<Telefone> paraListaTelefones(List<TelefoneDTO> telefoneDTOS) {

        // Utiliza Stream + Map para converter cada telefone da lista.
        return telefoneDTOS.stream()
                .map(this::paraTelefone)
                .toList();
    }


    // Converte um TelefoneDTO para uma Entity Telefone.
    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {

        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }


    // Converte uma Entity Usuario para um UsuarioDTO.
    public UsuarioDTO paraUsuarioDTO(Usuario usuarioDTO) {

        return UsuarioDTO.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(usuarioDTO.getEnderecos() != null ?
                        paraListaEnderecoDTO(usuarioDTO.getEnderecos()) : null)
                .telefones(usuarioDTO.getTelefones() != null ?
                        paraListaTelefonesDTO(usuarioDTO.getTelefones()) : null)
                .build();
    }


    // Converte uma lista de Endereco para uma lista de EnderecoDTO.
    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecos) {

        // Cria uma lista para armazenar os endereços convertidos.
        List<EnderecoDTO> enderecosDTO = new ArrayList<>();

        // Percorre cada endereço e realiza sua conversão para DTO.
        for (Endereco endereco : enderecos) {
            enderecosDTO.add(paraEnderecoDTO(endereco));
        }

        return enderecosDTO;
    }


    // Converte uma Entity Endereco para um EnderecoDTO.
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


    // Converte uma lista de Telefone para uma lista de TelefoneDTO.
    public List<TelefoneDTO> paraListaTelefonesDTO(List<Telefone> telefones) {

        // Utiliza Stream + Map para converter cada telefone da lista.
        return telefones.stream()
                .map(this::paraTelefoneDTO)
                .toList();
    }


    // Converte uma Entity Telefone para um TelefoneDTO.
    public TelefoneDTO paraTelefoneDTO(Telefone telefone) {

        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }


    // Atualiza os dados do usuário mantendo os campos não informados.
    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity) {

        return Usuario.builder()

                // Atualiza o nome ou mantém o valor atual.
                .nome(usuarioDTO.getNome() != null ?
                        usuarioDTO.getNome() : entity.getNome())

                // Mantém o ID original da Entity.
                .id(entity.getId())

                // Atualiza a senha ou mantém a senha atual.
                .senha(usuarioDTO.getSenha() != null ?
                        usuarioDTO.getSenha() : entity.getSenha())

                // Atualiza o e-mail ou mantém o e-mail atual.
                .email(usuarioDTO.getEmail() != null ?
                        usuarioDTO.getEmail() : entity.getEmail())

                // Mantém os endereços já cadastrados.
                .enderecos(entity.getEnderecos())

                // Mantém os telefones já cadastrados.
                .telefones(entity.getTelefones())

                .build();
    }


    // Atualiza os dados do endereço mantendo os campos não informados.
    public Endereco updateEndereco(EnderecoDTO dto, Endereco entity) {

        return Endereco.builder()
                .id(entity.getId())

                // Atualiza a rua ou mantém o valor atual.
                .rua(dto.getRua() != null ?
                        dto.getRua() : entity.getRua())

                // Atualiza o número ou mantém o valor atual.
                .numero(dto.getNumero() != null ?
                        dto.getNumero() : entity.getNumero())

                // Atualiza a cidade ou mantém o valor atual.
                .cidade(dto.getCidade() != null ?
                        dto.getCidade() : entity.getCidade())

                // Atualiza o CEP ou mantém o valor atual.
                .cep(dto.getCep() != null ?
                        dto.getCep() : entity.getCep())

                // Atualiza o complemento ou mantém o valor atual.
                .complemento(dto.getComplemento() != null ?
                        dto.getComplemento() : entity.getComplemento())

                // Atualiza o estado ou mantém o valor atual.
                .estado(dto.getEstado() != null ?
                        dto.getEstado() : entity.getEstado())

                .build();
    }


    // Atualiza os dados do telefone mantendo os campos não informados.
    public Telefone updateTelefone(TelefoneDTO dto, Telefone entity) {

        return Telefone.builder()
                .id(entity.getId())

                // Atualiza o DDD ou mantém o valor atual.
                .ddd(dto.getDdd() != null ?
                        dto.getDdd() : entity.getDdd())

                // Atualiza o número ou mantém o valor atual.
                .numero(dto.getNumero() != null ?
                        dto.getNumero() : entity.getNumero())

                .build();
    }


    // Converte um EnderecoDTO para Entity e associa ao usuário.
    public Endereco paraEnderecoEntity(EnderecoDTO dto, Long idUsuario) {

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


    // Converte um TelefoneDTO para Entity e associa ao usuário.
    public Telefone paraTelefoneEntity(TelefoneDTO dto, Long idUsuario) {

        return Telefone.builder()
                .numero(dto.getNumero())
                .ddd(dto.getDdd())
                .usuario_id(idUsuario)
                .build();
    }
}
