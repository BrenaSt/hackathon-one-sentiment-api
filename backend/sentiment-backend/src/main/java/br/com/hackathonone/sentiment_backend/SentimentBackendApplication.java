package br.com.hackathonone.sentiment_backend;

import br.com.hackathonone.sentiment_backend.domain.Cliente;
import br.com.hackathonone.sentiment_backend.domain.Produto;
import br.com.hackathonone.sentiment_backend.domain.enums.TipoCliente;
import br.com.hackathonone.sentiment_backend.repository.ClienteRepository;
import br.com.hackathonone.sentiment_backend.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Aplicação principal do Sentiment Backend API.
 */
@Slf4j
@SpringBootApplication
@EnableFeignClients
public class SentimentBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentimentBackendApplication.class, args);
    }

    /**
     * Inicializa dados de demonstração no perfil dev.
     */
    @Bean
    @Profile("dev")
    CommandLineRunner initDevData(ClienteRepository clienteRepository, ProdutoRepository produtoRepository) {
        return args -> {
            log.info("Inicializando dados de demonstração...");

            // Cria vendedor de demonstração
            Cliente vendedor = clienteRepository.save(Cliente.builder()
                .nome("Loja Demo")
                .email("vendedor@demo.com")
                .tipoCliente(TipoCliente.CLIENTE_VENDEDOR)
                .criadoEm(LocalDateTime.now())
                .build());
            log.info("Vendedor criado: ID={}", vendedor.getId());

            // Cria comprador de demonstração
            Cliente comprador = clienteRepository.save(Cliente.builder()
                .nome("João Comprador")
                .email("comprador@demo.com")
                .tipoCliente(TipoCliente.CLIENTE_COMPRADOR)
                .criadoEm(LocalDateTime.now())
                .build());
            log.info("Comprador criado: ID={}", comprador.getId());

            // Cria produtos de demonstração
            Produto produto1 = produtoRepository.save(Produto.builder()
                .nome("Smartphone XYZ Pro")
                .preco(new BigDecimal("2499.99"))
                .categoria("Eletrônicos")
                .tags("smartphone,celular,android")
                .descricao("Smartphone de última geração com câmera de 108MP")
                .imagemUrl("https://via.placeholder.com/300x300?text=Smartphone")
                .clienteVendedor(vendedor)
                .build());
            log.info("Produto criado: ID={}, Nome={}", produto1.getId(), produto1.getNome());

            Produto produto2 = produtoRepository.save(Produto.builder()
                .nome("Fone de Ouvido Bluetooth")
                .preco(new BigDecimal("299.90"))
                .categoria("Acessórios")
                .tags("fone,bluetooth,audio")
                .descricao("Fone de ouvido sem fio com cancelamento de ruído")
                .imagemUrl("https://via.placeholder.com/300x300?text=Fone")
                .clienteVendedor(vendedor)
                .build());
            log.info("Produto criado: ID={}, Nome={}", produto2.getId(), produto2.getNome());

            Produto produto3 = produtoRepository.save(Produto.builder()
                .nome("Camiseta Básica")
                .preco(new BigDecimal("49.90"))
                .categoria("Vestuário")
                .tags("camiseta,roupa,basica")
                .descricao("Camiseta 100% algodão, várias cores")
                .imagemUrl("https://via.placeholder.com/300x300?text=Camiseta")
                .clienteVendedor(vendedor)
                .build());
            log.info("Produto criado: ID={}, Nome={}", produto3.getId(), produto3.getNome());

            log.info("Dados de demonstração inicializados com sucesso!");
            log.info("Total de clientes: {}", clienteRepository.count());
            log.info("Total de produtos: {}", produtoRepository.count());
        };
    }
}
