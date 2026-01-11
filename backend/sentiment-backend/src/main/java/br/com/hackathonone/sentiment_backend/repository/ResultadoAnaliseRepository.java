package br.com.hackathonone.sentiment_backend.repository;

import br.com.hackathonone.sentiment_backend.domain.ResultadoAnalise;
import br.com.hackathonone.sentiment_backend.domain.enums.Sentimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência de Resultados de Análise.
 */
public interface ResultadoAnaliseRepository extends JpaRepository<ResultadoAnalise, Long> {

    /**
     * Busca resultado por comentário.
     */
    Optional<ResultadoAnalise> findByComentarioId(Long comentarioId);

    /**
     * Busca resultados por sentimento.
     */
    List<ResultadoAnalise> findBySentimento(Sentimento sentimento);

    /**
     * Busca resultados críticos.
     */
    List<ResultadoAnalise> findByEhCriticoTrue();

    /**
     * Conta resultados por sentimento de um vendedor.
     */
    @Query("SELECT COUNT(r) FROM ResultadoAnalise r " +
           "WHERE r.comentario.produto.clienteVendedor.id = :vendedorId " +
           "AND r.sentimento = :sentimento")
    long countByVendedorIdAndSentimento(
        @Param("vendedorId") Long vendedorId, 
        @Param("sentimento") Sentimento sentimento
    );

    /**
     * Busca resultados de análise de um vendedor.
     */
    @Query("SELECT r FROM ResultadoAnalise r " +
           "WHERE r.comentario.produto.clienteVendedor.id = :vendedorId " +
           "ORDER BY r.dataAnalise DESC")
    List<ResultadoAnalise> findByVendedorId(@Param("vendedorId") Long vendedorId);

    /**
     * Conta total de análises de um vendedor.
     */
    @Query("SELECT COUNT(r) FROM ResultadoAnalise r " +
           "WHERE r.comentario.produto.clienteVendedor.id = :vendedorId")
    long countByVendedorId(@Param("vendedorId") Long vendedorId);
}
