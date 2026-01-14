package com.sentimentapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para resposta de análise de sentimento em lote (batch).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchSentimentResponse {

    @JsonProperty("batch_id")
    private String batchId;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("resultados")
    private List<BatchItemResponse> resultados;

    @JsonProperty("tempo_total_ms")
    private Long tempoTotalMs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BatchItemResponse {

        @JsonProperty("texto")
        private String texto;

        @JsonProperty("previsao")
        private String previsao;

        @JsonProperty("probabilidade")
        private Double probabilidade;
    }
}
