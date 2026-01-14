package com.sentimentapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentimentapi.dto.request.BatchSentimentRequest;
import com.sentimentapi.dto.request.SentimentRequest;
import com.sentimentapi.dto.response.BatchSentimentResponse;
import com.sentimentapi.dto.response.SentimentResponse;
import com.sentimentapi.dto.response.StatsResponse;
import com.sentimentapi.service.DsServiceClient;
import com.sentimentapi.dto.DsServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para a API de Sentimento.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class SentimentApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DsServiceClient dsServiceClient;

    @Test
    @DisplayName("Fluxo completo: análise de sentimento e verificação de estatísticas")
    void fluxoCompletoAnaliseEStats() throws Exception {
        // Mock do DS Service
        when(dsServiceClient.predict(anyString()))
                .thenReturn(new DsServiceResponse("Positivo", 0.85));

        // 1. Enviar análise de sentimento
        SentimentRequest request = new SentimentRequest("Produto muito bom, entrega rápida!");

        MvcResult result = mockMvc.perform(post("/api/v1/sentiment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        SentimentResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SentimentResponse.class
        );

        assertThat(response.getPrevisao()).isEqualTo("Positivo");
        assertThat(response.getProbabilidade()).isEqualTo(0.85);

        // 2. Verificar estatísticas
        MvcResult statsResult = mockMvc.perform(get("/api/v1/stats"))
                .andExpect(status().isOk())
                .andReturn();

        StatsResponse stats = objectMapper.readValue(
                statsResult.getResponse().getContentAsString(),
                StatsResponse.class
        );

        assertThat(stats.getTotalAnalises()).isGreaterThanOrEqualTo(1);
        assertThat(stats.getPositivos()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Batch processing: múltiplos textos")
    void batchProcessingMultiplosTextos() throws Exception {
        // Mock do DS Service para diferentes respostas
        when(dsServiceClient.predict("Excelente produto!"))
                .thenReturn(new DsServiceResponse("Positivo", 0.95));
        when(dsServiceClient.predict("Produto ruim, não recomendo"))
                .thenReturn(new DsServiceResponse("Negativo", 0.88));
        when(dsServiceClient.predict("Produto normal, nada demais"))
                .thenReturn(new DsServiceResponse("Neutro", 0.65));

        BatchSentimentRequest batchRequest = BatchSentimentRequest.builder()
                .texts(List.of(
                        new SentimentRequest("Excelente produto!"),
                        new SentimentRequest("Produto ruim, não recomendo"),
                        new SentimentRequest("Produto normal, nada demais")
                ))
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/sentiment/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batchRequest)))
                .andExpect(status().isOk())
                .andReturn();

        BatchSentimentResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BatchSentimentResponse.class
        );

        assertThat(response.getTotal()).isEqualTo(3);
        assertThat(response.getBatchId()).isNotNull();
        assertThat(response.getResultados()).hasSize(3);
    }

    @Test
    @DisplayName("Health check deve retornar status UP")
    void healthCheckDeveRetornarStatusUp() throws Exception {
        when(dsServiceClient.isHealthy()).thenReturn(true);

        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk());
    }
}
