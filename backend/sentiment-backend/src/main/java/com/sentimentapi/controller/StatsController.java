package com.sentimentapi.controller;

import com.sentimentapi.dto.response.StatsResponse;
import com.sentimentapi.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para estatísticas de análises de sentimento.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Stats", description = "Endpoints para estatísticas de análises")
public class StatsController {

    private final StatsService statsService;

    /**
     * Retorna estatísticas consolidadas de todas as análises.
     */
    @GetMapping("/stats")
    @Operation(
            summary = "Obter estatísticas de análises",
            description = "Retorna estatísticas consolidadas incluindo total de análises, distribuição por sentimento e métricas de desempenho"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estatísticas obtidas com sucesso",
                    content = @Content(schema = @Schema(implementation = StatsResponse.class))
            )
    })
    public ResponseEntity<StatsResponse> getStats() {
        log.info("Requisição de estatísticas recebida");

        StatsResponse stats = statsService.getStats();

        return ResponseEntity.ok(stats);
    }
}
