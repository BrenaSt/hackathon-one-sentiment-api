package br.com.hackathonone.sentiment_backend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de resposta para estatísticas do Dashboard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private Long vendedorId;
    private long totalComentarios;
    private long totalProdutos;
    private long comentariosPositivos;
    private long comentariosNegativos;
    private long comentariosNeutros;
    private double percentualPositivo;
    private double percentualNegativo;
    private double percentualNeutro;
    private long notificacoesPendentes;
    private LocalDateTime dataConsulta;
}
