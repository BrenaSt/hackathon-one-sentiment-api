package br.com.hackathonone.sentiment_backend.service;

import br.com.hackathonone.sentiment_backend.domain.Cliente;
import br.com.hackathonone.sentiment_backend.domain.Comentario;
import br.com.hackathonone.sentiment_backend.domain.Produto;
import br.com.hackathonone.sentiment_backend.domain.ResultadoAnalise;
import br.com.hackathonone.sentiment_backend.domain.enums.Sentimento;
import br.com.hackathonone.sentiment_backend.dto.api.ComentarioRequest;
import br.com.hackathonone.sentiment_backend.dto.api.ComentarioResponse;
import br.com.hackathonone.sentiment_backend.dto.ds.DsPredictRequest;
import br.com.hackathonone.sentiment_backend.dto.ds.DsPredictResponse;
import br.com.hackathonone.sentiment_backend.client.SentimentDsClient;
import br.com.hackathonone.sentiment_backend.repository.ClienteRepository;
import br.com.hackathonone.sentiment_backend.repository.ComentarioRepository;
import br.com.hackathonone.sentiment_backend.repository.ProdutoRepository;
import br.com.hackathonone.sentiment_backend.repository.ResultadoAnaliseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para operações de negócio relacionadas a Comentários.
 * Integra com o serviço de Data Science para análise de sentimento.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ResultadoAnaliseRepository resultadoAnaliseRepository;
    private final SentimentDsClient dsClient;
    private final NotificacaoService notificacaoService;

    // Threshold para considerar um comentário como crítico
    private static final double THRESHOLD_CRITICO = 0.8;

    /**
     * Cria um novo comentário e analisa o sentimento.
     */
    @Transactional
    public ComentarioResponse criar(ComentarioRequest request) {
        log.info("Criando comentário para produto ID: {}", request.getProdutoId());

        // Busca o produto
        Produto produto = produtoRepository.findById(request.getProdutoId())
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + request.getProdutoId()));

        // Busca o comprador (opcional)
        Cliente comprador = null;
        if (request.getCompradorId() != null) {
            comprador = clienteRepository.findById(request.getCompradorId())
                .orElse(null);
        }

        // Cria o comentário
        Comentario comentario = Comentario.builder()
            .textoOriginal(request.getTexto())
            .nota(request.getNota())
            .origem(request.getOrigem() != null ? request.getOrigem() : "SITE")
            .idioma("pt-BR")
            .dataCriacao(LocalDateTime.now())
            .clienteComprador(comprador)
            .produto(produto)
            .build();

        comentario = comentarioRepository.save(comentario);
        log.info("Comentário criado com ID: {}", comentario.getId());

        // Analisa o sentimento
        ResultadoAnalise resultado = analisarSentimento(comentario);

        // Se for negativo e crítico, cria notificação para o vendedor
        if (resultado.getSentimento() == Sentimento.NEGATIVO && resultado.getEhCritico()) {
            notificacaoService.criarNotificacaoComentarioCritico(
                produto.getClienteVendedor(),
                resultado
            );
        }

        return toResponse(comentario, resultado);
    }

    /**
     * Analisa o sentimento de um comentário usando o serviço de DS.
     */
    private ResultadoAnalise analisarSentimento(Comentario comentario) {
        log.info("Analisando sentimento do comentário ID: {}", comentario.getId());

        try {
            // Chama o serviço de Data Science
            DsPredictRequest dsRequest = new DsPredictRequest(comentario.getTextoOriginal());
            DsPredictResponse dsResponse = dsClient.predict(dsRequest);

            log.info("Resposta do DS: label={}, probability={}", 
                dsResponse.getLabel(), dsResponse.getProbability());

            // Mapeia o label para o enum Sentimento
            Sentimento sentimento = mapearSentimento(dsResponse.getLabel());

            // Determina se é crítico
            boolean ehCritico = sentimento == Sentimento.NEGATIVO 
                && dsResponse.getProbability() >= THRESHOLD_CRITICO;

            // Cria o resultado da análise
            ResultadoAnalise resultado = ResultadoAnalise.builder()
                .sentimento(sentimento)
                .probabilidade(dsResponse.getProbability())
                .ehCritico(ehCritico)
                .dataAnalise(LocalDateTime.now())
                .comentario(comentario)
                .build();

            return resultadoAnaliseRepository.save(resultado);

        } catch (Exception e) {
            log.error("Erro ao analisar sentimento: {}", e.getMessage());
            
            // Em caso de erro, salva como NEUTRO com probabilidade baixa
            ResultadoAnalise resultado = ResultadoAnalise.builder()
                .sentimento(Sentimento.NEUTRO)
                .probabilidade(0.5)
                .ehCritico(false)
                .dataAnalise(LocalDateTime.now())
                .comentario(comentario)
                .build();

            return resultadoAnaliseRepository.save(resultado);
        }
    }

    /**
     * Mapeia o label retornado pelo DS para o enum Sentimento.
     */
    private Sentimento mapearSentimento(String label) {
        if (label == null) return Sentimento.NEUTRO;
        
        return switch (label.toUpperCase()) {
            case "POSITIVO", "POSITIVE", "POS" -> Sentimento.POSITIVO;
            case "NEGATIVO", "NEGATIVE", "NEG" -> Sentimento.NEGATIVO;
            default -> Sentimento.NEUTRO;
        };
    }

    /**
     * Busca comentário por ID.
     */
    @Transactional(readOnly = true)
    public ComentarioResponse buscarPorId(Long id) {
        Comentario comentario = comentarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comentário não encontrado: " + id));
        
        ResultadoAnalise resultado = resultadoAnaliseRepository.findByComentarioId(id)
            .orElse(null);
        
        return toResponse(comentario, resultado);
    }

    /**
     * Lista comentários de um produto.
     */
    @Transactional(readOnly = true)
    public List<ComentarioResponse> listarPorProduto(Long produtoId) {
        return comentarioRepository.findByProdutoId(produtoId).stream()
            .map(c -> {
                ResultadoAnalise resultado = resultadoAnaliseRepository.findByComentarioId(c.getId())
                    .orElse(null);
                return toResponse(c, resultado);
            })
            .collect(Collectors.toList());
    }

    /**
     * Lista comentários de produtos de um vendedor.
     */
    @Transactional(readOnly = true)
    public List<ComentarioResponse> listarPorVendedor(Long vendedorId) {
        return comentarioRepository.findByVendedorId(vendedorId).stream()
            .map(c -> {
                ResultadoAnalise resultado = resultadoAnaliseRepository.findByComentarioId(c.getId())
                    .orElse(null);
                return toResponse(c, resultado);
            })
            .collect(Collectors.toList());
    }

    /**
     * Converte entidade para DTO de resposta.
     */
    private ComentarioResponse toResponse(Comentario comentario, ResultadoAnalise resultado) {
        ComentarioResponse.ComentarioResponseBuilder builder = ComentarioResponse.builder()
            .id(comentario.getId())
            .texto(comentario.getTextoOriginal())
            .nota(comentario.getNota())
            .origem(comentario.getOrigem())
            .dataCriacao(comentario.getDataCriacao())
            .produtoId(comentario.getProduto().getId())
            .produtoNome(comentario.getProduto().getNome());

        if (comentario.getClienteComprador() != null) {
            builder.compradorId(comentario.getClienteComprador().getId())
                   .compradorNome(comentario.getClienteComprador().getNome());
        }

        if (resultado != null) {
            builder.sentimento(resultado.getSentimento().name())
                   .probabilidade(resultado.getProbabilidade())
                   .ehCritico(resultado.getEhCritico());
        }

        return builder.build();
    }
}
