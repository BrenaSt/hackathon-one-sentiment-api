package br.com.hackathonone.sentiment_backend.dto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requisição para análise de sentimento direta.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentRequest {

    @NotBlank(message = "O texto é obrigatório")
    @Size(min = 3, message = "O texto deve ter pelo menos 3 caracteres")
    private String text;
}
