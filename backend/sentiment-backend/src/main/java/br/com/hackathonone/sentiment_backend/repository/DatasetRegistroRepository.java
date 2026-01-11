package br.com.hackathonone.sentiment_backend.repository;

import br.com.hackathonone.sentiment_backend.domain.DatasetRegistro;
import br.com.hackathonone.sentiment_backend.domain.enums.SplitDataset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório para operações de persistência de Registros de Dataset.
 */
public interface DatasetRegistroRepository extends JpaRepository<DatasetRegistro, Long> {

    /**
     * Busca registros por split (TRAIN, TEST, VALID).
     */
    List<DatasetRegistro> findBySplit(SplitDataset split);

    /**
     * Busca registros por fonte.
     */
    List<DatasetRegistro> findByFonte(String fonte);

    /**
     * Conta registros por split.
     */
    long countBySplit(SplitDataset split);
}
