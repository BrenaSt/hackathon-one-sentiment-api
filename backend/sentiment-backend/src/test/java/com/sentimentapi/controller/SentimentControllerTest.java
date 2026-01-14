package com.sentimentapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentimentapi.dto.request.SentimentRequest;
import com.sentimentapi.dto.response.SentimentResponse;
import com.sentimentapi.service.SentimentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para o SentimentController.
 */
@WebMvcTest(SentimentController.class)
class SentimentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SentimentService sentimentService;

    @Test
    @DisplayName("Deve retornar sentimento positivo para texto positivo")
    void deveRetornarSentimentoPositivo() throws Exception {
        // Arrange
        SentimentRequest request = new SentimentRequest("Produto excelente! Recomendo a todos.");
        SentimentResponse response = SentimentResponse.builder()
                .previsao("Positivo")
                .probabilidade(0.92)
                .build();

        when(sentimentService.analisar(any(SentimentRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/sentiment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previsao").value("Positivo"))
                .andExpect(jsonPath("$.probabilidade").value(0.92));
    }

    @Test
    @DisplayName("Deve retornar sentimento negativo para texto negativo")
    void deveRetornarSentimentoNegativo() throws Exception {
        // Arrange
        SentimentRequest request = new SentimentRequest("Péssimo atendimento, produto com defeito.");
        SentimentResponse response = SentimentResponse.builder()
                .previsao("Negativo")
                .probabilidade(0.87)
                .build();

        when(sentimentService.analisar(any(SentimentRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/sentiment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previsao").value("Negativo"))
                .andExpect(jsonPath("$.probabilidade").value(0.87));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando texto está vazio")
    void deveRetornarErroQuandoTextoVazio() throws Exception {
        // Arrange
        SentimentRequest request = new SentimentRequest("");

        // Act & Assert
        mockMvc.perform(post("/api/v1/sentiment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando texto é muito curto")
    void deveRetornarErroQuandoTextoMuitoCurto() throws Exception {
        // Arrange
        SentimentRequest request = new SentimentRequest("ab");

        // Act & Assert
        mockMvc.perform(post("/api/v1/sentiment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro quando body está ausente")
    void deveRetornarErroQuandoBodyAusente() throws Exception {
        // Act & Assert - Spring retorna 400 ou 500 dependendo da versão
        mockMvc.perform(post("/api/v1/sentiment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
