package br.com.hackathonone.sentiment_backend.repository;

import br.com.hackathonone.sentiment_backend.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório para operações de persistência de Produtos.
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /**
     * Busca produtos por vendedor.
     */
    List<Produto> findByClienteVendedorId(Long vendedorId);

    /**
     * Busca produtos por categoria.
     */
    List<Produto> findByCategoria(String categoria);

    /**
     * Busca produtos por nome (contém, case insensitive).
     */
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    /**
     * Conta produtos de um vendedor.
     */
    long countByClienteVendedorId(Long vendedorId);
}
