package com.sentimentapi.domain.entity;

import com.sentimentapi.domain.enums.Sentimento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa o resultado de uma análise de sentimento.
 * Persiste cada requisição para estatísticas e histórico.
 */
@Entity
@Table(name = "analise_resultado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnaliseResultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto_original", columnDefinition = "TEXT", nullable = false)
    private String textoOriginal;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentimento", length = 20, nullable = false)
    private Sentimento sentimento;

    @Column(name = "probabilidade", nullable = false)
    private Double probabilidade;

    @Column(name = "data_analise", nullable = false)
    private LocalDateTime dataAnalise;

    @Column(name = "tempo_processamento_ms")
    private Long tempoProcessamentoMs;

    @Column(name = "origem", length = 50)
    private String origem;

    @Column(name = "batch_id", length = 100)
    private String batchId;

    @PrePersist
    protected void onCreate() {
        if (dataAnalise == null) {
            dataAnalise = LocalDateTime.now();
        }
    }
}
