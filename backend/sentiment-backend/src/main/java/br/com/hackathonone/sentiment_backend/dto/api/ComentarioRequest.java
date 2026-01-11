package br.com.hackathonone.sentiment_backend.dto.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requisição para criação de Comentário.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioRequest {

    @NotBlank(message = "O texto do comentário é obrigatório")
    @Size(min = 3, message = "O comentário deve ter pelo menos 3 caracteres")
    private String texto;

    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer nota;

    private String origem; // SITE, APP, OUTRO

    @NotNull(message = "O ID do produto é obrigatório")
    private Long produtoId;

    private Long compradorId; // Opcional
}
