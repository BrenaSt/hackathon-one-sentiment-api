package br.com.hackathonone.sentiment_backend.controller;

import br.com.hackathonone.sentiment_backend.dto.api.ComentarioRequest;
import br.com.hackathonone.sentiment_backend.dto.api.ComentarioResponse;
import br.com.hackathonone.sentiment_backend.service.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações de Comentários.
 * Integra automaticamente com análise de sentimento.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    /**
     * Cria um novo comentário e analisa o sentimento automaticamente.
     */
    @PostMapping
    public ResponseEntity<ComentarioResponse> criar(@Valid @RequestBody ComentarioRequest request) {
        log.info("Criando comentário para produto ID: {}", request.getProdutoId());
        ComentarioResponse response = comentarioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca comentário por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponse> buscarPorId(@PathVariable Long id) {
        log.info("Buscando comentário ID: {}", id);
        ComentarioResponse response = comentarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista comentários por produto.
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<ComentarioResponse>> listarPorProduto(@PathVariable Long produtoId) {
        log.info("Listando comentários do produto ID: {}", produtoId);
        List<ComentarioResponse> response = comentarioService.listarPorProduto(produtoId);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista comentários de produtos de um vendedor.
     */
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<ComentarioResponse>> listarPorVendedor(@PathVariable Long vendedorId) {
        log.info("Listando comentários do vendedor ID: {}", vendedorId);
        List<ComentarioResponse> response = comentarioService.listarPorVendedor(vendedorId);
        return ResponseEntity.ok(response);
    }
}
