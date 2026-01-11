package br.com.hackathonone.sentiment_backend.repository;

import br.com.hackathonone.sentiment_backend.domain.Notificacao;
import br.com.hackathonone.sentiment_backend.domain.enums.StatusNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório para operações de persistência de Notificações.
 */
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    /**
     * Busca notificações por vendedor.
     */
    List<Notificacao> findByVendedorIdOrderByDataCriacaoDesc(Long vendedorId);

    /**
     * Busca notificações por vendedor e status.
     */
    List<Notificacao> findByVendedorIdAndStatusOrderByDataCriacaoDesc(
        Long vendedorId, 
        StatusNotificacao status
    );

    /**
     * Conta notificações pendentes de um vendedor.
     */
    long countByVendedorIdAndStatus(Long vendedorId, StatusNotificacao status);

    /**
     * Busca notificações não lidas de um vendedor.
     */
    default List<Notificacao> findPendentesByVendedorId(Long vendedorId) {
        return findByVendedorIdAndStatusOrderByDataCriacaoDesc(vendedorId, StatusNotificacao.PENDENTE);
    }
}
