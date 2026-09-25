
package com.javanauta.usuario.business.controller;

import com.javanauta.usuario.business.UsuarioService;
import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.osgi.annotation.bundle.Header;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    /*
     * ==========================================================
     * INJEÇÃO DE DEPENDÊNCIA
     * ==========================================================
     *
     */
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    /*
     * ==========================================================
     * CADASTRO DE USUÁRIO
     * ==========================================================
     *
     * @PostMapping
     *
     * Indica que esse método será executado quando a aplicação
     * receber uma requisição HTTP do tipo POST.
     *
     * Como a classe possui:
     *
     * @RequestMapping("/usuario")
     *
     * o endpoint completo será:
     *
     * POST /usuario
     *
     *
     * @RequestBody Indica que o corpo (Body) da requisição HTTP deve ser
     * convertido pelo Spring para um objeto UsuarioDTO.
     *
     * Exemplo de JSON enviado pelo Postman:
     *
     * {
     *     "nome": "Eduardo",
     *     "email": "eduardo@email.com",
     *     "senha": "1234567890"
     * }
     *
     * O Spring recebe esse JSON e transforma em:
     * UsuarioDTO usuarioDTO
     *
     *
     * ResponseEntity<UsuarioDTO> Representa a resposta HTTP da API.
     * Nesse caso, estamos informando que o corpo da resposta
     * será um objeto UsuarioDTO.
     *
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(
            @RequestBody UsuarioDTO usuarioDTO) {

        /*
         * Enviamos o UsuarioDTO para o Service.
         *
         * O Controller não deve concentrar as regras de negócio.
         * Sua responsabilidade principal é receber a requisição,
         * encaminhar os dados para o Service e devolver a resposta.
         */
        UsuarioDTO usuarioSalvo = usuarioService.salvaUsuario(usuarioDTO);

        /*
         * ResponseEntity.ok()
         *
         * Retorna HTTP 200 (OK) juntamente com o UsuarioDTO
         * retornado pelo Service.
         */
        return ResponseEntity.ok(usuarioSalvo);
    }

    /*
     * ==================
     * LOGIN
     * ==================
     * */

    // Define o endpoint: POST /usuario/login
    // Utilizado para autenticar o usuário e gerar o JWT.

    // @RequestBody recebe o JSON enviado pelo cliente
    // e transforma os dados em um UsuarioDTO.

    @PostMapping("/login")

    // DTO é utilizado para controlar(filtrar) quais dados entram ou saem da API,
    // evitando expor diretamente todos os atributos da entidade para o cliente
    // os DTos podem ser tanto de request ( recebem dados) ou response(envio de dados)
    public String login(@RequestBody UsuarioDTO usuarioDTO) {

        // Solicita ao AuthenticationManager que autentique o usuário.
        Authentication authentication = authenticationManager.authenticate(
                // Cria um objeto contendo as credenciais do usuário. //
                // email → identifica o usuário
                // senha → senha informada no login.
                new UsernamePasswordAuthenticationToken(
                        usuarioDTO.getEmail(),
                        usuarioDTO.getSenha()
                )
        );

        // Gera um JWT utilizando o nome do usuário autenticado.
        // authentication.getName() retorna o username autenticado.
        // Neste projeto, o username é o email.
        // "Bearer " informa ao cliente que o valor seguinte é um
        // token utilizado no padrão Bearer Authentication.

        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    /*
     * ====================
     * BUSCAR USUÁRIO
     * ====================
     * */

    // Define este método como um endpoint HTTP GET.
    // GET /usuario?email=usuario@email.com
    @GetMapping
    // O valor "joao@email.com" será armazenado na variável email.
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestParam("email")
                                                         String email){
        // Envia o email para o Service realizar a busca.
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }


    /*
     * =======================
     * DELETAR USUÁRIO
     * =======================
     *
     * */

    // Define este método como um endpoint HTTP DELETE.
    // DELETE /usuario/{email}
    // @PathVariable pega o valor diretamente da URL.
    // Exemplo:
    // DELETE /usuario/joao@email.com
    // O valor "joao@email.com" será colocado na variável email.
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        // Solicita ao Service a exclusão do usuário pelo email.
        usuarioService.deletaUsuarioPorEmail(email);
        // Retorna HTTP 200 sem conteúdo no corpo da resposta.
        return ResponseEntity.ok().build();
    }

    /*
     * =======================
     * ATUALIZAR DADOS
     * =======================
     */

    // Endpoint responsável por atualizar os dados do usuário autenticado.
        @PutMapping
        public ResponseEntity<UsuarioDTO> atualizaDadoUsuario(
                @RequestBody UsuarioDTO dto,
                @RequestHeader("Authorization") String token) {

            // Envia o DTO e o token para o Service realizar a atualização.
            return ResponseEntity.ok(
                    usuarioService.atualizarDadosUsuario(token, dto)
            );
        }

        @PutMapping("/endereco")
        public ResponseEntity<EnderecoDTO> atualizaEndereco(@RequestBody EnderecoDTO dto ,
                                                            @RequestParam("id") Long id){
            return ResponseEntity.ok(usuarioService.atualizaEndereco(id,dto));
        }

        @PutMapping("/telefone")
        public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO dto ,
                                                            @RequestParam("id") Long id){
            return ResponseEntity.ok(usuarioService.atualizaTelefone(id,dto));
        }


        @PostMapping("/endereco")
        public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO dto ,
                                                            @RequestHeader("Authorization") String token){
            return ResponseEntity.ok(usuarioService.cadastraEndereco(token,dto));
        }

        @PostMapping("/telefone")
        public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO dto ,
                                                            @RequestHeader("Authorization") String token){
            return ResponseEntity.ok(usuarioService.cadastraTelefone(token,dto));
        }




}

