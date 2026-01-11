package br.com.hackathonone.sentiment_backend.controller;

import br.com.hackathonone.sentiment_backend.dto.api.NotificacaoResponse;
import br.com.hackathonone.sentiment_backend.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para operações de Notificações.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    /**
     * Lista notificações de um vendedor.
     */
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<NotificacaoResponse>> listarPorVendedor(
            @PathVariable Long vendedorId,
            @RequestParam(defaultValue = "false") boolean apenasNaoLidas) {
        log.info("Listando notificações do vendedor ID: {}, apenasNaoLidas: {}", 
            vendedorId, apenasNaoLidas);
        
        List<NotificacaoResponse> response = apenasNaoLidas
            ? notificacaoService.listarPendentesPorVendedor(vendedorId)
            : notificacaoService.listarPorVendedor(vendedorId);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Conta notificações pendentes de um vendedor.
     */
    @GetMapping("/vendedor/{vendedorId}/count")
    public ResponseEntity<Map<String, Long>> contarPendentes(@PathVariable Long vendedorId) {
        log.info("Contando notificações pendentes do vendedor ID: {}", vendedorId);
        long count = notificacaoService.contarPendentesPorVendedor(vendedorId);
        return ResponseEntity.ok(Map.of("pendentes", count));
    }

    /**
     * Marca uma notificação como lida.
     */
    @PatchMapping("/{id}/lida")
    public ResponseEntity<NotificacaoResponse> marcarComoLida(@PathVariable Long id) {
        log.info("Marcando notificação ID: {} como lida", id);
        NotificacaoResponse response = notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Marca todas as notificações de um vendedor como lidas.
     */
    @PatchMapping("/vendedor/{vendedorId}/lidas")
    public ResponseEntity<Void> marcarTodasComoLidas(@PathVariable Long vendedorId) {
        log.info("Marcando todas as notificações do vendedor ID: {} como lidas", vendedorId);
        notificacaoService.marcarTodasComoLidas(vendedorId);
        return ResponseEntity.noContent().build();
    }
}
