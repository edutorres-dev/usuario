package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.exception.ConflictException;
import com.javanauta.usuario.infrastructure.exception.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuario.infrastructure.repository.TelefoneRepository;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    // Repository responsável pelo acesso aos dados do usuário.
    private final UsuarioRepository usuarioRepository;

    // Converter responsável pelas conversões entre DTO e Entity.
    private final UsuarioConverter usuarioConverter;

    // Responsável por criptografar as senhas dos usuários.
    private final PasswordEncoder passwordEncoder;

    // Responsável por gerar e extrair informações do JWT.
    private final JwtUtil jwtUtil;

    // Repository responsável pelo acesso aos endereços.
    private final EnderecoRepository enderecoRepository;

    // Repository responsável pelo acesso aos telefones.
    private final TelefoneRepository telefoneRepository;


    // Cadastra um novo usuário no banco de dados.
    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {

        // Verifica se o e-mail já está cadastrado.
        emailExiste(usuarioDTO.getEmail());

        // Criptografa a senha antes de salvar no banco.
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        // Converte o DTO recebido para uma Entity.
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);

        // Salva a Entity no banco de dados.
        usuario = usuarioRepository.save(usuario);

        // Converte a Entity salva novamente para DTO.
        return usuarioConverter.paraUsuarioDTO(usuario);
    }


    // Verifica se o e-mail já está cadastrado.
    public void emailExiste(String email) {

        try {
            // Consulta o banco para verificar se o e-mail existe.
            boolean existe = verificaEmailExistente(email);

            // Interrompe o cadastro caso o e-mail já exista.
            if (existe) {

                // Informa que o e-mail já está cadastrado.
                throw new ConflictException("Email já cadastrado" + email);
            }

        } catch (ConflictException e) {

            throw new ConflictException(
                    "Email já cadastrado" + e.getCause()
            );
        }
    }


    // Consulta o Repository para verificar se o e-mail existe.
    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }


    // Busca um usuário pelo e-mail informado.
    public UsuarioDTO buscarUsuarioPorEmail(String email) {

        try {

            // Busca o usuário no banco pelo e-mail.
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email).orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Email não encontrado" + email
                            )
                    )
            );

        } catch (ResourceNotFoundException e) {

            throw new ResourceNotFoundException(
                    "Email não encontrado" + email
            );
        }
    }


    // Exclui um usuário pelo e-mail informado.
    public void deletaUsuarioPorEmail(String email) {

        // Solicita ao Repository a exclusão do usuário.
        usuarioRepository.deleteByEmail(email);
    }


    // Atualiza os dados do usuário autenticado.
    public UsuarioDTO atualizarDadosUsuario(String token, UsuarioDTO dto) {

        // Remove "Bearer " e extrai o e-mail do token JWT.
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // Criptografa a nova senha caso ela seja informada.
        dto.setSenha(
                dto.getSenha() != null
                        ? passwordEncoder.encode(dto.getSenha())
                        : null
        );

        // Busca o usuário pelo e-mail extraído do token.
        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email não localizado")
                );

        // Atualiza a Entity com os dados recebidos no DTO.
        Usuario usuario = usuarioConverter.updateUsuario(
                dto,
                usuarioEntity
        );

        // Salva a Entity atualizada e retorna o DTO.
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }


    // Atualiza um endereço existente pelo ID.
    public EnderecoDTO atualizaEndereco(
            Long idEndereco,
            EnderecoDTO enderecoDTO) {

        // Busca o endereço pelo ID ou lança uma exceção.
        Endereco entity = enderecoRepository.findById(idEndereco)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "id não encontrado" + idEndereco
                        )
                );

        // Atualiza a Entity com os dados recebidos.
        Endereco endereco = usuarioConverter.updateEndereco(
                enderecoDTO,
                entity
        );

        // Salva e converte o endereço atualizado para DTO.
        return usuarioConverter.paraEnderecoDTO(
                enderecoRepository.save(endereco)
        );
    }


    // Atualiza um telefone existente pelo ID.
    public TelefoneDTO atualizaTelefone(
            Long idTelefone,
            TelefoneDTO dto) {

        // Busca o telefone pelo ID ou lança uma exceção.
        Telefone entity = telefoneRepository.findById(idTelefone)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "id não encontrado" + idTelefone
                        )
                );

        // Atualiza a Entity com os dados recebidos.
        Telefone telefone = usuarioConverter.updateTelefone(
                dto,
                entity
        );

        // Salva e converte o telefone atualizado para DTO.
        return usuarioConverter.paraTelefoneDTO(
                telefoneRepository.save(telefone)
        );
    }


    // Cadastra um novo endereço para o usuário autenticado.
    public EnderecoDTO cadastraEndereco(
            String token,
            EnderecoDTO dto) {

        // Extrai o e-mail do usuário através do token JWT.
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // Busca o usuário associado ao e-mail.
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Email não encontrado" + email
                        )
                );

        // Converte o DTO para Entity e associa ao usuário.
        Endereco endereco = usuarioConverter.paraEnderecoEntity(
                dto,
                usuario.getId()
        );

        // Salva o endereço no banco.
        Endereco enderecoEntity = enderecoRepository.save(endereco);

        // Converte a Entity salva para DTO.
        return usuarioConverter.paraEnderecoDTO(enderecoEntity);
    }


    // Cadastra um novo telefone para o usuário autenticado.
    public TelefoneDTO cadastraTelefone(
            String token,
            TelefoneDTO dto) {

        // Extrai o e-mail do usuário através do token JWT.
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // Busca o usuário associado ao e-mail.
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Email não encontrado" + email
                        )
                );

        // Converte o DTO para Entity e associa ao usuário.
        Telefone telefone = usuarioConverter.paraTelefoneEntity(
                dto,
                usuario.getId()
        );

        // Salva o telefone no banco.
        Telefone telefoneEntity = telefoneRepository.save(telefone);

        // Converte a Entity salva para DTO.
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);
    }
}