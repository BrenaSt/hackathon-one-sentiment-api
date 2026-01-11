package br.com.hackathonone.sentiment_backend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de resposta para Produto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private String imagemUrl;
    private String categoria;
    private String tags;
    private String descricao;
    private Long vendedorId;
    private String vendedorNome;
}
