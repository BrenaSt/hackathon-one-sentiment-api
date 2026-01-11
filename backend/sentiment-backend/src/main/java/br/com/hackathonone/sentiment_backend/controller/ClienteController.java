package br.com.hackathonone.sentiment_backend.controller;

import br.com.hackathonone.sentiment_backend.domain.enums.TipoCliente;
import br.com.hackathonone.sentiment_backend.dto.api.ClienteRequest;
import br.com.hackathonone.sentiment_backend.dto.api.ClienteResponse;
import br.com.hackathonone.sentiment_backend.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações de Clientes.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Cria um novo cliente.
     */
    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest request) {
        log.info("Criando cliente: {}", request.getNome());
        ClienteResponse response = clienteService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca cliente por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        log.info("Buscando cliente ID: {}", id);
        ClienteResponse response = clienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os clientes.
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos(
            @RequestParam(required = false) String tipo) {
        log.info("Listando clientes, tipo: {}", tipo);
        
        List<ClienteResponse> response;
        if (tipo != null && !tipo.isBlank()) {
            response = clienteService.listarPorTipo(TipoCliente.valueOf(tipo));
        } else {
            response = clienteService.listarTodos();
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Busca cliente por email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponse> buscarPorEmail(@PathVariable String email) {
        log.info("Buscando cliente por email: {}", email);
        ClienteResponse response = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um cliente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ClienteRequest request) {
        log.info("Atualizando cliente ID: {}", id);
        ClienteResponse response = clienteService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um cliente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        log.info("Removendo cliente ID: {}", id);
        clienteService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
