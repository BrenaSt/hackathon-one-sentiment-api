package br.com.hackathonone.sentiment_backend.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para análise de sentimento.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResponse {

    private String previsao; // Positivo, Negativo, Neutro
    private Double probabilidade;
}
