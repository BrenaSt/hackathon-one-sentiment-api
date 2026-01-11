package br.com.hackathonone.sentiment_backend.controller;

import br.com.hackathonone.sentiment_backend.dto.api.ProdutoRequest;
import br.com.hackathonone.sentiment_backend.dto.api.ProdutoResponse;
import br.com.hackathonone.sentiment_backend.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações de Produtos.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    /**
     * Cria um novo produto.
     */
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest request) {
        log.info("Criando produto: {}", request.getNome());
        ProdutoResponse response = produtoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca produto por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        log.info("Buscando produto ID: {}", id);
        ProdutoResponse response = produtoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os produtos ou filtra por vendedor/categoria.
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar(
            @RequestParam(required = false) Long vendedorId,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String nome) {
        log.info("Listando produtos - vendedorId: {}, categoria: {}, nome: {}", 
            vendedorId, categoria, nome);
        
        List<ProdutoResponse> response;
        
        if (vendedorId != null) {
            response = produtoService.listarPorVendedor(vendedorId);
        } else if (categoria != null && !categoria.isBlank()) {
            response = produtoService.listarPorCategoria(categoria);
        } else if (nome != null && !nome.isBlank()) {
            response = produtoService.buscarPorNome(nome);
        } else {
            response = produtoService.listarTodos();
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um produto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ProdutoRequest request) {
        log.info("Atualizando produto ID: {}", id);
        ProdutoResponse response = produtoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um produto.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        log.info("Removendo produto ID: {}", id);
        produtoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
