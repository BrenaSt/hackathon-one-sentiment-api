package br.com.hackathonone.sentiment_backend.service;

import br.com.hackathonone.sentiment_backend.domain.Cliente;
import br.com.hackathonone.sentiment_backend.domain.Produto;
import br.com.hackathonone.sentiment_backend.domain.enums.TipoCliente;
import br.com.hackathonone.sentiment_backend.dto.api.ProdutoRequest;
import br.com.hackathonone.sentiment_backend.dto.api.ProdutoResponse;
import br.com.hackathonone.sentiment_backend.exception.BusinessException;
import br.com.hackathonone.sentiment_backend.repository.ClienteRepository;
import br.com.hackathonone.sentiment_backend.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para operações de negócio relacionadas a Produtos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    /**
     * Cria um novo produto.
     */
    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        log.info("Criando produto: {} para vendedor ID: {}", request.getNome(), request.getVendedorId());

        Cliente vendedor = clienteRepository.findById(request.getVendedorId())
            .orElseThrow(() -> new EntityNotFoundException("Vendedor não encontrado: " + request.getVendedorId()));

        // Valida se é um vendedor
        if (vendedor.getTipoCliente() != TipoCliente.CLIENTE_VENDEDOR) {
            throw new BusinessException("Cliente não é um vendedor: " + request.getVendedorId());
        }

        Produto produto = Produto.builder()
            .nome(request.getNome())
            .preco(request.getPreco())
            .imagemUrl(request.getImagemUrl())
            .categoria(request.getCategoria())
            .tags(request.getTags())
            .descricao(request.getDescricao())
            .clienteVendedor(vendedor)
            .build();

        produto = produtoRepository.save(produto);
        log.info("Produto criado com ID: {}", produto.getId());

        return toResponse(produto);
    }

    /**
     * Busca produto por ID.
     */
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
        return toResponse(produto);
    }

    /**
     * Lista todos os produtos.
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarTodos() {
        return produtoRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Lista produtos de um vendedor.
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarPorVendedor(Long vendedorId) {
        return produtoRepository.findByClienteVendedorId(vendedorId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Lista produtos por categoria.
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Busca produtos por nome.
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Atualiza um produto existente.
     */
    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        log.info("Atualizando produto ID: {}", id);

        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));

        produto.setNome(request.getNome());
        produto.setPreco(request.getPreco());
        produto.setImagemUrl(request.getImagemUrl());
        produto.setCategoria(request.getCategoria());
        produto.setTags(request.getTags());
        produto.setDescricao(request.getDescricao());

        produto = produtoRepository.save(produto);
        return toResponse(produto);
    }

    /**
     * Remove um produto.
     */
    @Transactional
    public void remover(Long id) {
        log.info("Removendo produto ID: {}", id);

        if (!produtoRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto não encontrado: " + id);
        }

        produtoRepository.deleteById(id);
    }

    /**
     * Converte entidade para DTO de resposta.
     */
    private ProdutoResponse toResponse(Produto produto) {
        return ProdutoResponse.builder()
            .id(produto.getId())
            .nome(produto.getNome())
            .preco(produto.getPreco())
            .imagemUrl(produto.getImagemUrl())
            .categoria(produto.getCategoria())
            .tags(produto.getTags())
            .descricao(produto.getDescricao())
            .vendedorId(produto.getClienteVendedor().getId())
            .vendedorNome(produto.getClienteVendedor().getNome())
            .build();
    }
}
