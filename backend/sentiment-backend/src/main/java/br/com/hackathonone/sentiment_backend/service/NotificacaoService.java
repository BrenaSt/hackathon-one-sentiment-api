package br.com.hackathonone.sentiment_backend.service;

import br.com.hackathonone.sentiment_backend.domain.Cliente;
import br.com.hackathonone.sentiment_backend.domain.Notificacao;
import br.com.hackathonone.sentiment_backend.domain.ResultadoAnalise;
import br.com.hackathonone.sentiment_backend.domain.enums.CanalNotificacao;
import br.com.hackathonone.sentiment_backend.domain.enums.StatusNotificacao;
import br.com.hackathonone.sentiment_backend.dto.api.NotificacaoResponse;
import br.com.hackathonone.sentiment_backend.repository.NotificacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para operações de negócio relacionadas a Notificações.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    /**
     * Cria uma notificação para comentário crítico.
     */
    @Transactional
    public Notificacao criarNotificacaoComentarioCritico(Cliente vendedor, ResultadoAnalise resultado) {
        log.info("Criando notificação de comentário crítico para vendedor ID: {}", vendedor.getId());

        String mensagem = String.format(
            "Comentário crítico detectado no produto '%s'. Sentimento: %s (%.0f%% de certeza)",
            resultado.getComentario().getProduto().getNome(),
            resultado.getSentimento().name(),
            resultado.getProbabilidade() * 100
        );

        Notificacao notificacao = Notificacao.builder()
            .mensagem(mensagem)
            .status(StatusNotificacao.PENDENTE)
            .canal(CanalNotificacao.DASHBOARD)
            .dataCriacao(LocalDateTime.now())
            .vendedor(vendedor)
            .resultado(resultado)
            .build();

        notificacao = notificacaoRepository.save(notificacao);
        log.info("Notificação criada com ID: {}", notificacao.getId());

        return notificacao;
    }

    /**
     * Lista notificações de um vendedor.
     */
    @Transactional(readOnly = true)
    public List<NotificacaoResponse> listarPorVendedor(Long vendedorId) {
        return notificacaoRepository.findByVendedorIdOrderByDataCriacaoDesc(vendedorId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Lista notificações pendentes de um vendedor.
     */
    @Transactional(readOnly = true)
    public List<NotificacaoResponse> listarPendentesPorVendedor(Long vendedorId) {
        return notificacaoRepository.findPendentesByVendedorId(vendedorId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Conta notificações pendentes de um vendedor.
     */
    @Transactional(readOnly = true)
    public long contarPendentesPorVendedor(Long vendedorId) {
        return notificacaoRepository.countByVendedorIdAndStatus(vendedorId, StatusNotificacao.PENDENTE);
    }

    /**
     * Marca uma notificação como lida.
     */
    @Transactional
    public NotificacaoResponse marcarComoLida(Long id) {
        log.info("Marcando notificação ID: {} como lida", id);

        Notificacao notificacao = notificacaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada: " + id));

        notificacao.setStatus(StatusNotificacao.LIDA);
        notificacao = notificacaoRepository.save(notificacao);

        return toResponse(notificacao);
    }

    /**
     * Marca todas as notificações de um vendedor como lidas.
     */
    @Transactional
    public void marcarTodasComoLidas(Long vendedorId) {
        log.info("Marcando todas as notificações do vendedor ID: {} como lidas", vendedorId);

        List<Notificacao> pendentes = notificacaoRepository
            .findByVendedorIdAndStatusOrderByDataCriacaoDesc(vendedorId, StatusNotificacao.PENDENTE);

        pendentes.forEach(n -> n.setStatus(StatusNotificacao.LIDA));
        notificacaoRepository.saveAll(pendentes);
    }

    /**
     * Converte entidade para DTO de resposta.
     */
    private NotificacaoResponse toResponse(Notificacao notificacao) {
        return NotificacaoResponse.builder()
            .id(notificacao.getId())
            .mensagem(notificacao.getMensagem())
            .status(notificacao.getStatus().name())
            .canal(notificacao.getCanal().name())
            .dataCriacao(notificacao.getDataCriacao())
            .dataEnvio(notificacao.getDataEnvio())
            .vendedorId(notificacao.getVendedor().getId())
            .comentarioId(notificacao.getResultado().getComentario().getId())
            .produtoId(notificacao.getResultado().getComentario().getProduto().getId())
            .produtoNome(notificacao.getResultado().getComentario().getProduto().getNome())
            .build();
    }
}
