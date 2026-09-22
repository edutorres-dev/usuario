package com.javanauta.usuario.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*

 Filtro responsável por verificar o JWT enviado nas requisições.
 Ele é executado antes do Controller e verifica se a requisição
 possui um token JWT válido.

 Se o token for válido, o usuário é considerado autenticado
 e o Spring Security permite que a requisição continue.

 responsável por interceptar as requisições
 e trabalhar com o token JWT.

 */



// OncePerRequestFilter é uma classe do Spring usada
// para criar filtros que devem ser executados uma única vez
// por requisição HTTP.
public class JwtRequestFilter extends OncePerRequestFilter {

    // Define propriedades para armazenar instâncias de JwtUtil e UserDetailsService :

    // Para extrair informações e validar o JWT.
    private final JwtUtil jwtUtil;

    // Utilizado para buscar os dados do usuário no banco.
    private final UserDetailsService userDetailsService;

    /// Construtor utilizado pelo Spring para receber as dependências.
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Executa o filtro uma vez para cada requisição HTTP.
    // request → contém os dados enviados pelo cliente.
    // response → representa a resposta que será enviada ao cliente.
    // chain → permite continuar para o próximo filtro.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Obtém o valor do header "Authorization" da requisição
        // ex : Authorization: Bearer eyJhbGciOiJIUzI1Ni...
        final String authorizationHeader = request.getHeader("Authorization");

        // Verifica se o cabeçalho existe e começa com "Bearer "
        // Bearer indica que o cliente está enviand um token de
        // autenticação.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            // Extrai o token JWT do cabeçalho
            // Remove "Bearer " e pega somente o JWT.
            // substring(7) começa a partir do caractere 7,
            final String token = authorizationHeader.substring(7);

            // Extrai do JWT o username do usuário.
            // Neste projeto, o username é o email
            final String username = jwtUtil.extrairEmailToken(token);

            // Se o nome de usuário não for nulo e o usuário não estiver autenticado ainda
            // Isso evita autenticar novamente um usuário que //
            // já foi autenticado nesta requisição.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carrega os detalhes do usuário a partir do nome de usuário
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Valida o token JWT
                // Verifica se o JWT é válido e pertence
                // ao usuário identificado pelo username.
                if (jwtUtil.validateToken(token, username)) {
                    // Cria um objeto de autenticação com as informações do usuário
                    // userDetails → dados do usuário.
                    // null → não precisamos da senha aqui.
                    // authorities → roles/permissões do usuário.
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Define a autenticação no contexto de segurança
                    // Registra o usuário como autenticado
                    // A partir daqui, o Spring Security sabe
                    // quem é o usuário que fez a requisição.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // Continua a execução da cadeia de filtros.
        // Se o token for válido, a requisição continua autenticada.
        // Depois dos filtros, ela poderá chegar ao Controller.
        chain.doFilter(request, response);
    }
}
