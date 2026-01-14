package com.sentimentapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de análise de sentimento.
 * Segue o contrato definido no escopo do hackathon.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentimentResponse {

    @JsonProperty("previsao")
    private String previsao;

    @JsonProperty("probabilidade")
    private Double probabilidade;
}
