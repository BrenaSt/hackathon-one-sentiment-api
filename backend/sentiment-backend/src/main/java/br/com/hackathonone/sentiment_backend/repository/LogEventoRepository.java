package br.com.hackathonone.sentiment_backend.repository;

import br.com.hackathonone.sentiment_backend.domain.LogEvento;
import br.com.hackathonone.sentiment_backend.domain.enums.NivelLog;
import br.com.hackathonone.sentiment_backend.domain.enums.OrigemLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para operações de persistência de Logs de Evento.
 */
public interface LogEventoRepository extends JpaRepository<LogEvento, Long> {

    /**
     * Busca logs por nível.
     */
    List<LogEvento> findByNivel(NivelLog nivel);

    /**
     * Busca logs por origem.
     */
    List<LogEvento> findByOrigem(OrigemLog origem);

    /**
     * Busca logs por período.
     */
    List<LogEvento> findByDataEventoBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca logs de erro recentes.
     */
    List<LogEvento> findByNivelAndDataEventoAfterOrderByDataEventoDesc(
        NivelLog nivel, 
        LocalDateTime dataInicio
    );
}
