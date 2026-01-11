package br.com.hackathonone.sentiment_backend.dto.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requisição para criação/atualização de Cliente.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Email(message = "Email inválido")
    @Size(max = 150, message = "O email deve ter no máximo 150 caracteres")
    private String email;

    @NotBlank(message = "O tipo de cliente é obrigatório")
    @Pattern(regexp = "CLIENTE_COMPRADOR|CLIENTE_VENDEDOR|ADMIN", 
             message = "Tipo de cliente deve ser: CLIENTE_COMPRADOR, CLIENTE_VENDEDOR ou ADMIN")
    private String tipoCliente;
}
