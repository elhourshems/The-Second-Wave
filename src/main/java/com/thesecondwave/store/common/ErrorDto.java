package com.thesecondwave.store.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Schema(name = "Error", description = "Standard error response returned when a request fails")
public class ErrorDto {
    @Schema(description = "Human-readable error message", example = "Product not found")
    private String error;
}
