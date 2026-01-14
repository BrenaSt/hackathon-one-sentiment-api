package com.sentimentapi.controller;

import com.sentimentapi.service.DsServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para verificação de saúde da aplicação.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Endpoints para verificação de saúde")
public class HealthController {

    private final DsServiceClient dsServiceClient;

    /**
     * Verifica a saúde da aplicação e suas dependências.
     */
    @GetMapping("/health")
    @Operation(
            summary = "Verificar saúde da aplicação",
            description = "Retorna o status da aplicação e de suas dependências (DS Service)"
    )
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "sentiment-backend");

        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("ds-service", dsServiceClient.isHealthy() ? "UP" : "DOWN");
        health.put("dependencies", dependencies);

        return ResponseEntity.ok(health);
    }
}
