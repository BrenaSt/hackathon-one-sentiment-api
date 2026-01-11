package br.com.hackathonone.sentiment_backend.dto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de requisição para criação/atualização de Produto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
    private String nome;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    private BigDecimal preco;

    @Size(max = 255, message = "A URL da imagem deve ter no máximo 255 caracteres")
    private String imagemUrl;

    @Size(max = 100, message = "A categoria deve ter no máximo 100 caracteres")
    private String categoria;

    @Size(max = 255, message = "As tags devem ter no máximo 255 caracteres")
    private String tags;

    private String descricao;

    @NotNull(message = "O ID do vendedor é obrigatório")
    private Long vendedorId;
}
