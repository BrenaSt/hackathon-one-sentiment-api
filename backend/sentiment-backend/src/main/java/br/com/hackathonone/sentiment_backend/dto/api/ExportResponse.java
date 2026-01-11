package br.com.hackathonone.sentiment_backend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de resposta para exportação de dados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportResponse {

    private Long vendedorId;
    private LocalDateTime dataExportacao;
    private DashboardStatsResponse estatisticas;
    private List<ComentarioResponse> comentarios;
}
