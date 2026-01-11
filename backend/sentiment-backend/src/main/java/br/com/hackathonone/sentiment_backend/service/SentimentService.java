package br.com.hackathonone.sentiment_backend.service;

import br.com.hackathonone.sentiment_backend.client.SentimentDsClient;
import br.com.hackathonone.sentiment_backend.dto.api.SentimentRequest;
import br.com.hackathonone.sentiment_backend.dto.api.SentimentResponse;
import br.com.hackathonone.sentiment_backend.dto.ds.DsPredictRequest;
import br.com.hackathonone.sentiment_backend.dto.ds.DsPredictResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço para análise de sentimento.
 * Faz a ponte entre a API pública e o serviço de Data Science.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SentimentService {

    private final SentimentDsClient dsClient;

    /**
     * Analisa o sentimento de um texto.
     * Este endpoint é para análise direta, sem persistência.
     */
    public SentimentResponse analisarSentimento(SentimentRequest request) {
        log.info("Analisando sentimento para texto: {}", 
            request.getText().substring(0, Math.min(50, request.getText().length())) + "...");

        // Chama o serviço de Data Science
        DsPredictRequest dsRequest = new DsPredictRequest(request.getText());
        DsPredictResponse dsResponse = dsClient.predict(dsRequest);

        log.info("Resposta do DS: label={}, probability={}", 
            dsResponse.getLabel(), dsResponse.getProbability());

        // Mapeia para o contrato público
        return SentimentResponse.builder()
            .previsao(mapearLabel(dsResponse.getLabel()))
            .probabilidade(dsResponse.getProbability())
            .build();
    }

    /**
     * Mapeia o label do DS para o formato esperado pela API.
     */
    private String mapearLabel(String label) {
        if (label == null) return "Neutro";
        
        return switch (label.toUpperCase()) {
            case "POSITIVO", "POSITIVE", "POS" -> "Positivo";
            case "NEGATIVO", "NEGATIVE", "NEG" -> "Negativo";
            case "NEUTRO", "NEUTRAL", "NEU" -> "Neutro";
            default -> label;
        };
    }
}
