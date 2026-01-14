package com.sentimentapi.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para o enum Sentimento.
 */
class SentimentoTest {

    @ParameterizedTest
    @DisplayName("Deve converter labels positivos corretamente")
    @CsvSource({
            "Positivo, POSITIVO",
            "POSITIVO, POSITIVO",
            "POSITIVE, POSITIVO",
            "POS, POSITIVO",
            "1, POSITIVO"
    })
    void deveConverterLabelsPositivos(String input, Sentimento expected) {
        assertThat(Sentimento.fromLabel(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Deve converter labels negativos corretamente")
    @CsvSource({
            "Negativo, NEGATIVO",
            "NEGATIVO, NEGATIVO",
            "NEGATIVE, NEGATIVO",
            "NEG, NEGATIVO",
            "0, NEGATIVO"
    })
    void deveConverterLabelsNegativos(String input, Sentimento expected) {
        assertThat(Sentimento.fromLabel(input)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Deve retornar NEUTRO para labels desconhecidos")
    void deveRetornarNeutroParaLabelsDesconhecidos() {
        assertThat(Sentimento.fromLabel("unknown")).isEqualTo(Sentimento.NEUTRO);
        assertThat(Sentimento.fromLabel("")).isEqualTo(Sentimento.NEUTRO);
        assertThat(Sentimento.fromLabel(null)).isEqualTo(Sentimento.NEUTRO);
    }

    @Test
    @DisplayName("Deve retornar label correto para cada sentimento")
    void deveRetornarLabelCorreto() {
        assertThat(Sentimento.POSITIVO.getLabel()).isEqualTo("Positivo");
        assertThat(Sentimento.NEGATIVO.getLabel()).isEqualTo("Negativo");
        assertThat(Sentimento.NEUTRO.getLabel()).isEqualTo("Neutro");
    }
}
