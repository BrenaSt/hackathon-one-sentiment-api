package br.com.hackathonone.sentiment_backend.controller;

import br.com.hackathonone.sentiment_backend.dto.api.DashboardStatsResponse;
import br.com.hackathonone.sentiment_backend.dto.api.ExportResponse;
import br.com.hackathonone.sentiment_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para Dashboard do Vendedor.
 * Fornece estatísticas e exportação de dados.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Obtém estatísticas do dashboard para um vendedor.
     */
    @GetMapping("/stats/{vendedorId}")
    public ResponseEntity<DashboardStatsResponse> getStats(@PathVariable Long vendedorId) {
        log.info("Obtendo estatísticas do dashboard para vendedor ID: {}", vendedorId);
        DashboardStatsResponse response = dashboardService.getStats(vendedorId);
        return ResponseEntity.ok(response);
    }

    /**
     * Exporta dados de comentários e análises de um vendedor.
     */
    @GetMapping("/export/{vendedorId}")
    public ResponseEntity<ExportResponse> exportar(@PathVariable Long vendedorId) {
        log.info("Exportando dados para vendedor ID: {}", vendedorId);
        ExportResponse response = dashboardService.exportar(vendedorId);
        return ResponseEntity.ok(response);
    }
}
