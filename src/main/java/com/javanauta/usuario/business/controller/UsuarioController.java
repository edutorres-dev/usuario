
package com.javanauta.usuario.business.controller;

import com.javanauta.usuario.business.UsuarioService;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    /*
     * ==========================================================
     * INJEÇÃO DE DEPENDÊNCIA
     * ==========================================================
     *
     * O UsuarioService contém as regras de negócio relacionadas
     * ao usuário.
     *
     * Através do "private final", declaramos a dependência que
     * o Controller precisa para executar suas operações.
     *
     * A anotação @RequiredArgsConstructor do Lombok cria
     * automaticamente um construtor contendo todos os atributos
     * final.
     *
     * Dessa forma, o Spring consegue fazer a injeção de
     * dependência através do construtor.
     */
    private final UsuarioService usuarioService;


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
}

