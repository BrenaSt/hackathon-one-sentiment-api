package br.com.hackathonone.sentiment_backend.service;

import br.com.hackathonone.sentiment_backend.domain.enums.Sentimento;
import br.com.hackathonone.sentiment_backend.dto.api.DashboardStatsResponse;
import br.com.hackathonone.sentiment_backend.dto.api.ExportResponse;
import br.com.hackathonone.sentiment_backend.dto.api.ComentarioResponse;
import br.com.hackathonone.sentiment_backend.repository.ComentarioRepository;
import br.com.hackathonone.sentiment_backend.repository.NotificacaoRepository;
import br.com.hackathonone.sentiment_backend.repository.ProdutoRepository;
import br.com.hackathonone.sentiment_backend.repository.ResultadoAnaliseRepository;
import br.com.hackathonone.sentiment_backend.domain.enums.StatusNotificacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço para operações do Dashboard do Vendedor.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ResultadoAnaliseRepository resultadoAnaliseRepository;
    private final ComentarioRepository comentarioRepository;
    private final ProdutoRepository produtoRepository;
    private final NotificacaoRepository notificacaoRepository;
    private final ComentarioService comentarioService;

    /**
     * Obtém estatísticas do dashboard para um vendedor.
     */
    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats(Long vendedorId) {
        log.info("Obtendo estatísticas do dashboard para vendedor ID: {}", vendedorId);

        long totalComentarios = resultadoAnaliseRepository.countByVendedorId(vendedorId);
        long positivos = resultadoAnaliseRepository.countByVendedorIdAndSentimento(vendedorId, Sentimento.POSITIVO);
        long negativos = resultadoAnaliseRepository.countByVendedorIdAndSentimento(vendedorId, Sentimento.NEGATIVO);
        long neutros = resultadoAnaliseRepository.countByVendedorIdAndSentimento(vendedorId, Sentimento.NEUTRO);
        long totalProdutos = produtoRepository.countByClienteVendedorId(vendedorId);
        long notificacoesPendentes = notificacaoRepository.countByVendedorIdAndStatus(vendedorId, StatusNotificacao.PENDENTE);

        // Calcula percentuais
        double percentPositivo = totalComentarios > 0 ? (double) positivos / totalComentarios * 100 : 0;
        double percentNegativo = totalComentarios > 0 ? (double) negativos / totalComentarios * 100 : 0;
        double percentNeutro = totalComentarios > 0 ? (double) neutros / totalComentarios * 100 : 0;

        return DashboardStatsResponse.builder()
            .vendedorId(vendedorId)
            .totalComentarios(totalComentarios)
            .totalProdutos(totalProdutos)
            .comentariosPositivos(positivos)
            .comentariosNegativos(negativos)
            .comentariosNeutros(neutros)
            .percentualPositivo(Math.round(percentPositivo * 100.0) / 100.0)
            .percentualNegativo(Math.round(percentNegativo * 100.0) / 100.0)
            .percentualNeutro(Math.round(percentNeutro * 100.0) / 100.0)
            .notificacoesPendentes(notificacoesPendentes)
            .dataConsulta(LocalDateTime.now())
            .build();
    }

    /**
     * Exporta dados de comentários e análises de um vendedor.
     */
    @Transactional(readOnly = true)
    public ExportResponse exportar(Long vendedorId) {
        log.info("Exportando dados para vendedor ID: {}", vendedorId);

        DashboardStatsResponse stats = getStats(vendedorId);
        List<ComentarioResponse> comentarios = comentarioService.listarPorVendedor(vendedorId);

        return ExportResponse.builder()
            .vendedorId(vendedorId)
            .dataExportacao(LocalDateTime.now())
            .estatisticas(stats)
            .comentarios(comentarios)
            .build();
    }
}
