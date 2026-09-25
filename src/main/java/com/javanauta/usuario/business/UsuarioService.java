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
    private final PasswordEncoder passwordEncoder; // responsável pela encriptação da senha
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
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

        emailExiste(usuarioDTO.getEmail()); // verifica se o email existe
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

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

    /*
     * =========================
     * VERIFICA O EMAIL
     * =========================
     * */
    public void emailExiste(String email){
        try{
            // Consulta o banco para verificar se o email existe.
            boolean existe = verificaEmailExistente(email);
            // se o email existe gera uma exception e interrompe o cadastro.
            if(existe){
                // Informa que o email já está cadastrado.
                throw new ConflictException("Email já cadastrado" + email);
            }
        }catch(ConflictException e ){
            throw new ConflictException( " Email já cadastrado" +e.getCause());
        }


    }

    /*
     * ===========================
     * VERIFICAR SE O EMAIL EXISTE
     * ===========================
     * */


    // Consulta o Repository e retorna se o email está cadastrado.
    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    /*
     * =======================
     * BUSCAR O USUÁRIO
     * =======================
     *
     * */

    // Busca um usuário pelo email.
    public UsuarioDTO buscarUsuarioPorEmail(String email){

        try {
            // Procura o usuário no banco pelo email.
            // Se encontrar → retorna o Usuario.
            // Se não encontrar → lança ResourceNotFoundException.
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    ()-> new ResourceNotFoundException("Email não encontrado" + email))
            );
        }catch (ResourceNotFoundException e ){
            throw new ResourceNotFoundException("Email não encontrado" + email);
        }

    }

    /*
     * ====================
     * DELETAR USUARIO
     * =====================
     * */

    // Exclui o usuário pelo email.
    public void deletaUsuarioPorEmail(String email){
        // Solicita ao Repository a exclusão do usuário
        usuarioRepository.deleteByEmail(email);
    }


    /*
     * ==============================
     * ATUALIZANDO DADOS DO USUÁRIO
     * ==============================
     */
    public UsuarioDTO atualizarDadosUsuario(String token, UsuarioDTO dto) {

        // Remove "Bearer " do token e extrai o e-mail do usuário autenticado.
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // Criptografa a nova senha somente se o usuário informar uma senha.
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        // Busca o usuário no banco pelo e-mail extraído do token.
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));

        // Atualiza a Entity com os dados recebidos através do DTO.
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        // Salva a Entity atualizada e converte o resultado novamente para DTO.
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }


    public EnderecoDTO atualizaEndereco(Long idEndereco , EnderecoDTO enderecoDTO){
            Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(()->
                    new ResourceNotFoundException("id não encontrado" + idEndereco));

            Endereco endereco= usuarioConverter.updateEndereco(enderecoDTO,entity);

           return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco)) ;

    }

    public TelefoneDTO atualizaTelefone(Long idTelefone , TelefoneDTO dto){
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(()->
                new ResourceNotFoundException("id não encontrado" + idTelefone));

        Telefone telefone= usuarioConverter.updateTelefone(dto,entity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone)) ;

    }


}
