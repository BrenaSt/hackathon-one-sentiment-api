package com.sentimentapi.domain.enums;

/**
 * Enum representando os tipos de sentimento classificados pelo modelo de ML.
 */
public enum Sentimento {
    POSITIVO("Positivo"),
    NEGATIVO("Negativo"),
    NEUTRO("Neutro");

    private final String label;

    Sentimento(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Converte uma string de label para o enum correspondente.
     * Aceita variações como "Positivo", "POSITIVO", "POSITIVE", etc.
     */
    public static Sentimento fromLabel(String label) {
        if (label == null) {
            return NEUTRO;
        }
        
        String normalized = label.trim().toUpperCase();
        
        return switch (normalized) {
            case "POSITIVO", "POSITIVE", "POS", "1" -> POSITIVO;
            case "NEGATIVO", "NEGATIVE", "NEG", "0" -> NEGATIVO;
            default -> NEUTRO;
        };
    }
}
