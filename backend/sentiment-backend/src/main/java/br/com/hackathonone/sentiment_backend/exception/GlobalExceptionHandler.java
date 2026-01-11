package br.com.hackathonone.sentiment_backend.exception;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Tratador global de exceções para a API.
 * Centraliza o tratamento de erros e retorna respostas JSON padronizadas.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata erros de validação (ex: @NotBlank, @Size).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        log.warn("Erro de validação: {}", ex.getMessage());
        
        Map<String, Object> body = buildErrorBody(
            HttpStatus.BAD_REQUEST,
            "Erro de validação",
            ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Dados inválidos",
            request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Trata erros de comunicação com o serviço de Data Science (Feign).
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeign(
            FeignException ex, 
            HttpServletRequest request) {
        
        log.error("Erro de comunicação com DS Service: {}", ex.getMessage());
        
        Map<String, Object> body = buildErrorBody(
            HttpStatus.SERVICE_UNAVAILABLE,
            "Serviço externo indisponível",
            "O serviço de Inteligência Artificial está temporariamente indisponível. Tente novamente em alguns instantes.",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    /**
     * Trata erros de entidade não encontrada.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(
            EntityNotFoundException ex, 
            HttpServletRequest request) {
        
        log.warn("Entidade não encontrada: {}", ex.getMessage());
        
        Map<String, Object> body = buildErrorBody(
            HttpStatus.NOT_FOUND,
            "Recurso não encontrado",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Trata exceções de negócio customizadas.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(
            BusinessException ex, 
            HttpServletRequest request) {
        
        log.warn("Erro de negócio: {}", ex.getMessage());
        
        Map<String, Object> body = buildErrorBody(
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Erro de negócio",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    /**
     * Trata qualquer outra exceção não prevista.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex, 
            HttpServletRequest request) {
        
        log.error("Erro inesperado: ", ex);
        
        Map<String, Object> body = buildErrorBody(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno",
            "Ocorreu um erro inesperado no servidor. Por favor, tente novamente.",
            request.getRequestURI()
        );

        return ResponseEntity.internalServerError().body(body);
    }

    /**
     * Constrói o corpo padronizado de resposta de erro.
     */
    private Map<String, Object> buildErrorBody(
            HttpStatus status, 
            String error, 
            String message, 
            String path) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return body;
    }
}
