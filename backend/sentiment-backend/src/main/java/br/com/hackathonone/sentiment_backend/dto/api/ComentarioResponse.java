package br.com.hackathonone.sentiment_backend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de resposta para Comentário com resultado da análise de sentimento.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioResponse {

    private Long id;
    private String texto;
    private Integer nota;
    private String origem;
    private LocalDateTime dataCriacao;
    
    // Dados do produto
    private Long produtoId;
    private String produtoNome;
    
    // Dados do comprador (opcional)
    private Long compradorId;
    private String compradorNome;
    
    // Resultado da análise de sentimento
    private String sentimento;
    private Double probabilidade;
    private Boolean ehCritico;
}
