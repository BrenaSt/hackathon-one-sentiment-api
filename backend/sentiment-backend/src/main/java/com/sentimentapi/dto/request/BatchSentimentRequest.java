package com.sentimentapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para requisição de análise de sentimento em lote (batch).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchSentimentRequest {

    @NotEmpty(message = "A lista de textos não pode estar vazia")
    @Size(max = 100, message = "Máximo de 100 textos por requisição")
    @Valid
    private List<SentimentRequest> texts;
}
