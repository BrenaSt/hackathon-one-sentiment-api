package br.com.hackathonone.sentiment_backend.controller;

import br.com.hackathonone.sentiment_backend.dto.api.SentimentRequest;
import br.com.hackathonone.sentiment_backend.dto.api.SentimentResponse;
import br.com.hackathonone.sentiment_backend.service.SentimentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para análise de sentimento direta.
 * Endpoint principal do MVP conforme especificação do hackathon.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/sentiment")
@RequiredArgsConstructor
public class SentimentController {

    private final SentimentService sentimentService;

    /**
     * Analisa o sentimento de um texto.
     * 
     * POST /api/v1/sentiment
     * Body: { "text": "Texto para análise" }
     * Response: { "previsao": "Positivo", "probabilidade": 0.85 }
     */
    @PostMapping
    public ResponseEntity<SentimentResponse> analisar(@Valid @RequestBody SentimentRequest request) {
        log.info("Recebida requisição de análise de sentimento");
        SentimentResponse response = sentimentService.analisarSentimento(request);
        return ResponseEntity.ok(response);
    }
}
