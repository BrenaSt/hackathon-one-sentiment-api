package com.sentimentapi.repository;

import com.sentimentapi.domain.entity.AnaliseResultado;
import com.sentimentapi.domain.enums.Sentimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para operações de persistência de análises de sentimento.
 */
@Repository
public interface AnaliseResultadoRepository extends JpaRepository<AnaliseResultado, Long> {

    /**
     * Conta análises por tipo de sentimento.
     */
    long countBySentimento(Sentimento sentimento);

    /**
     * Busca análises por batch ID.
     */
    List<AnaliseResultado> findByBatchId(String batchId);

    /**
     * Busca análises em um período de tempo.
     */
    List<AnaliseResultado> findByDataAnaliseBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Conta análises por sentimento em um período.
     */
    long countBySentimentoAndDataAnaliseBetween(Sentimento sentimento, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca as últimas N análises ordenadas por data.
     */
    List<AnaliseResultado> findTop100ByOrderByDataAnaliseDesc();

    /**
     * Calcula a média de probabilidade por sentimento.
     */
    @Query("SELECT AVG(a.probabilidade) FROM AnaliseResultado a WHERE a.sentimento = :sentimento")
    Double findAverageProbabilidadeBySentimento(Sentimento sentimento);

    /**
     * Calcula o tempo médio de processamento.
     */
    @Query("SELECT AVG(a.tempoProcessamentoMs) FROM AnaliseResultado a WHERE a.tempoProcessamentoMs IS NOT NULL")
    Double findAverageTempoProcessamento();
}
