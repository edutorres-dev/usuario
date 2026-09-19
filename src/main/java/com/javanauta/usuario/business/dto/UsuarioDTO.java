package com.javanauta.usuario.business.dto;
import lombok.*;
import java.util.List;

/*
 * ==========================================================
 * DTO - DATA TRANSFER OBJECT
 * ==========================================================
 *
 * DTO significa Data Transfer Object (Objeto de Transferência
 * de Dados).
 *
 * O DTO é utilizado para transportar informações entre as
 * diferentes camadas da aplicação.
 *
 * Neste caso, o UsuarioDTO representa os dados que podem ser
 * recebidos ou enviados pela API relacionados ao usuário.
 *
 * O DTO não representa diretamente uma tabela do banco de dados.
 * Essa responsabilidade pertence à Entity Usuario.
 *
 * Fluxo simplificado:
 *
 * JSON
 *   ↓
 * UsuarioDTO
 *   ↓
 * UsuarioConverter
 *   ↓
 * Usuario (Entity)
 *   ↓
 * UsuarioRepository
 *   ↓
 * Banco de dados
 *
 *
 * Não colocamos o "id" neste DTO porque, neste modelo, ele não
 * faz parte dos dados que precisamos receber do usuário durante
 * o cadastro.
 *
 * O ID é gerado pelo banco de dados através da Entity Usuario.
 *
 * Isso também ajuda a evitar que o cliente da API tente definir
 * manualmente o ID de um usuário durante o cadastro.
 *
 * A anotação @Builder do Lombok gera automaticamente o padrão
 * Builder para essa classe.
 *
 * Ele permite criar um UsuarioDTO de forma mais organizada,
 * por exemplo:
 *
 * UsuarioDTO usuarioDTO = UsuarioDTO.builder()
 *         .nome("Eduardo")
 *         .email("eduardo@email.com")
 *         .senha("1234567890")
 *         .build();
 *
 * No nosso projeto, o Builder também é utilizado pelo
 * UsuarioConverter durante as conversões entre DTO e Entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {

    private String nome;
    private String email;
    private String senha;
    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;
}

