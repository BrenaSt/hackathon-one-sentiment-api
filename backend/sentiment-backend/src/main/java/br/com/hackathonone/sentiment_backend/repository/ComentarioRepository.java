package br.com.hackathonone.sentiment_backend.repository;

import br.com.hackathonone.sentiment_backend.domain.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositório para operações de persistência de Comentários.
 */
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    /**
     * Busca comentários por produto.
     */
    List<Comentario> findByProdutoId(Long produtoId);

    /**
     * Busca comentários por comprador.
     */
    List<Comentario> findByClienteCompradorId(Long clienteCompradorId);

    /**
     * Busca comentários de produtos de um vendedor específico.
     */
    @Query("SELECT c FROM Comentario c WHERE c.produto.clienteVendedor.id = :vendedorId ORDER BY c.dataCriacao DESC")
    List<Comentario> findByVendedorId(@Param("vendedorId") Long vendedorId);

    /**
     * Conta comentários por produto.
     */
    long countByProdutoId(Long produtoId);
}
