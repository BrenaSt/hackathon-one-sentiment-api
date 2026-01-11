package br.com.hackathonone.sentiment_backend.repository;

import br.com.hackathonone.sentiment_backend.domain.ModeloML;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para operações de persistência de Modelos de ML.
 */
public interface ModeloMLRepository extends JpaRepository<ModeloML, Long> {

    /**
     * Busca o modelo ativo.
     */
    Optional<ModeloML> findByAtivoTrue();

    /**
     * Busca modelo por nome e versão.
     */
    Optional<ModeloML> findByNomeAndVersao(String nome, String versao);
}
