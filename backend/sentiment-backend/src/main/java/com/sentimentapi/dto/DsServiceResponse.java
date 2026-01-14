package com.sentimentapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mapear a resposta do microserviço de Data Science.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DsServiceResponse {

    @JsonProperty("label")
    private String label;

    @JsonProperty("probability")
    private Double probability;
}
