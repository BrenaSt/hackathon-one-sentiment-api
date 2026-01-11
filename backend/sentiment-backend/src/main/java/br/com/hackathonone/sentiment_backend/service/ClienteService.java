package br.com.hackathonone.sentiment_backend.service;

import br.com.hackathonone.sentiment_backend.domain.Cliente;
import br.com.hackathonone.sentiment_backend.domain.enums.TipoCliente;
import br.com.hackathonone.sentiment_backend.dto.api.ClienteRequest;
import br.com.hackathonone.sentiment_backend.dto.api.ClienteResponse;
import br.com.hackathonone.sentiment_backend.exception.BusinessException;
import br.com.hackathonone.sentiment_backend.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para operações de negócio relacionadas a Clientes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Cria um novo cliente.
     */
    @Transactional
    public ClienteResponse criar(ClienteRequest request) {
        log.info("Criando cliente: {}", request.getNome());

        // Verifica se email já existe
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            clienteRepository.findByEmail(request.getEmail())
                .ifPresent(c -> {
                    throw new BusinessException("Email já cadastrado: " + request.getEmail());
                });
        }

        Cliente cliente = Cliente.builder()
            .nome(request.getNome())
            .email(request.getEmail())
            .tipoCliente(TipoCliente.valueOf(request.getTipoCliente()))
            .criadoEm(LocalDateTime.now())
            .build();

        cliente = clienteRepository.save(cliente);
        log.info("Cliente criado com ID: {}", cliente.getId());

        return toResponse(cliente);
    }

    /**
     * Busca cliente por ID.
     */
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
        return toResponse(cliente);
    }

    /**
     * Lista todos os clientes.
     */
    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Lista clientes por tipo.
     */
    @Transactional(readOnly = true)
    public List<ClienteResponse> listarPorTipo(TipoCliente tipo) {
        return clienteRepository.findByTipoCliente(tipo).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Busca cliente por email.
     */
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + email));
        return toResponse(cliente);
    }

    /**
     * Atualiza um cliente existente.
     */
    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        log.info("Atualizando cliente ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));

        // Verifica se novo email já existe em outro cliente
        if (request.getEmail() != null && !request.getEmail().equals(cliente.getEmail())) {
            clienteRepository.findByEmail(request.getEmail())
                .ifPresent(c -> {
                    throw new BusinessException("Email já cadastrado: " + request.getEmail());
                });
        }

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTipoCliente(TipoCliente.valueOf(request.getTipoCliente()));

        cliente = clienteRepository.save(cliente);
        return toResponse(cliente);
    }

    /**
     * Remove um cliente.
     */
    @Transactional
    public void remover(Long id) {
        log.info("Removendo cliente ID: {}", id);

        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado: " + id);
        }

        clienteRepository.deleteById(id);
    }

    /**
     * Converte entidade para DTO de resposta.
     */
    private ClienteResponse toResponse(Cliente cliente) {
        return ClienteResponse.builder()
            .id(cliente.getId())
            .nome(cliente.getNome())
            .email(cliente.getEmail())
            .tipoCliente(cliente.getTipoCliente().name())
            .criadoEm(cliente.getCriadoEm())
            .build();
    }
}
