package br.com.hackathonone.sentiment_backend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de resposta para Notificação.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoResponse {

    private Long id;
    private String mensagem;
    private String status;
    private String canal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEnvio;
    private Long vendedorId;
    private Long comentarioId;
    private Long produtoId;
    private String produtoNome;
}
