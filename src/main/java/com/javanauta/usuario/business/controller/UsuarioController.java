
package com.javanauta.usuario.business.controller;

import com.javanauta.usuario.business.UsuarioService;
import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor

/**
 * Expõe os endpoints de gerenciamento de usuários do BFF.
 * Recebe as requisições HTTP e delega as operações ao UsuarioService.
 */


public class UsuarioController {

    // Service responsável pelas operações relacionadas ao usuário.
    private final UsuarioService usuarioService;

    // Responsável por realizar a autenticação do usuário.
    private final AuthenticationManager authenticationManager;

    // Responsável por gerar e manipular os tokens JWT.
    private final JwtUtil jwtUtil;

    // Endpoint responsável por cadastrar um novo usuário.
    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(
            @RequestBody UsuarioDTO usuarioDTO) {

        // Envia os dados recebidos para o Service realizar o cadastro.
        UsuarioDTO usuarioSalvo = usuarioService.salvaUsuario(usuarioDTO);

        // Retorna o usuário cadastrado com HTTP 200.
        return ResponseEntity.ok(usuarioSalvo);
    }

    // Endpoint responsável por autenticar o usuário e gerar o JWT.
    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {

        // Solicita ao AuthenticationManager a validação das credenciais.
        Authentication authentication = authenticationManager.authenticate(

                // Cria o objeto com e-mail e senha informados no login.
                new UsernamePasswordAuthenticationToken(
                        usuarioDTO.getEmail(),
                        usuarioDTO.getSenha()
                )
        );

        // Gera o JWT usando o e-mail do usuário autenticado.
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }



    // Endpoint responsável por buscar um usuário pelo e-mail.
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(
            @RequestParam("email") String email) {

        // Envia o e-mail para o Service realizar a busca.
        return ResponseEntity.ok(
                usuarioService.buscarUsuarioPorEmail(email)
        );
    }


    // Endpoint responsável por excluir um usuário pelo e-mail.
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(
            @PathVariable String email) {

        // Solicita ao Service a exclusão do usuário.
        usuarioService.deletaUsuarioPorEmail(email);

        // Retorna HTTP 200 sem conteúdo no corpo da resposta.
        return ResponseEntity.ok().build();
    }


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


    // Endpoint responsável por atualizar um endereço existente.
    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(
            @RequestBody EnderecoDTO dto,
            @RequestParam("id") Long id) {

        // Envia o endereço e o ID para o Service realizar a atualização.
        return ResponseEntity.ok(
                usuarioService.atualizaEndereco(id, dto)
        );
    }


    // Endpoint responsável por atualizar um telefone existente.
    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(
            @RequestBody TelefoneDTO dto,
            @RequestParam("id") Long id) {

        // Envia o telefone e o ID para o Service realizar a atualização.
        return ResponseEntity.ok(
                usuarioService.atualizaTelefone(id, dto)
        );
    }


    // Endpoint responsável por cadastrar um novo endereço.
    @PostMapping("/endereco")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(
            @RequestBody EnderecoDTO dto,
            @RequestHeader("Authorization") String token) {

        // Envia o endereço e o token para o Service realizar o cadastro.
        return ResponseEntity.ok(
                usuarioService.cadastraEndereco(token, dto)
        );
    }


    // Endpoint responsável por cadastrar um novo telefone.
    @PostMapping("/telefone")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(
            @RequestBody TelefoneDTO dto,
            @RequestHeader("Authorization") String token) {

        // Envia o telefone e o token para o Service realizar o cadastro.
        return ResponseEntity.ok(
                usuarioService.cadastraTelefone(token, dto)
        );
    }
}
