package com.javanauta.usuario.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Classe responsável por configurar o Spring Security da aplicação.
@Configuration

// Ativa e permite configurar o Spring Security na aplicação.
@EnableWebSecurity
public class SecurityConfig {

    // Utilizado para gerar e validar os tokens JWT.
    private final JwtUtil jwtUtil;

    // Utilizado pelo Spring Security para buscar os dados do usuário.
    private final UserDetailsService userDetailsService;

    // Construtor para injeção de dependências de JwtUtil e UserDetailsService
    // @Autowired é uma anotação do Spring usada para fazer injeção de dependência
    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Cria o Bean responsável por definir as regras de segurança da aplicação.
    // Aqui configuramos endpoints publicos e protegidos ,
    // sessão e filtro jwt
    @Bean

//    @Bean é uma anotação do Spring usada para registrar um objeto dentro
//    do container do Spring, para que o próprio Spring possa criar, gerenciar
//    e disponibilizar esse objeto para outras classes.

    // Cria o filtro responsável por verificar o JWT enviado nas requisições.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cria uma instância do JwtRequestFilter com JwtUtil e UserDetailsService
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable) // Desativa proteção CSRF para APIs REST
                // (não aplicável a APIs que não mantêm estado)
                .authorizeHttpRequests(authorize -> authorize // Define quais endpoints precisam de autenticação.
                    .requestMatchers("/usuario/login").permitAll() // Permite acesso ao endpoint de login sem autenticação
                    .requestMatchers(HttpMethod.GET, "/auth").permitAll()// Permite acesso ao endpoint GET /auth sem autenticação
                    .requestMatchers(HttpMethod.POST, "/usuario").permitAll() // Permite o cadastro de novos usuários sem autenticação.
                    .requestMatchers("/usuario/**").authenticated() // Exige autenticação para os demais endpoints que começam com /usuario/.
                    .anyRequest().authenticated() // Requer autenticação para todas as outras requisições
                )
                // Configura a aplicação para não utilizar sessão HTTP para manter o usuário autenticado.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura a política de sessão como stateless (sem sessão)
                )
                // Adiciona o filtro JWT antes do filtro de autenticação padrão
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Finaliza e retorna a configuração de segurança.
        return http.build();
    }

    // Cria o Bean responsável por tratar as senhas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt gera um hash seguro para armazenar a senha.
        return new BCryptPasswordEncoder();
    }

    // Cria o AuthenticationManager utilizado no processo de login.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Obtém o AuthenticationManager configurado pelo Spring.
        return authenticationConfiguration.getAuthenticationManager();
    }

}
