package com.Zest.product_assesment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Standard structure for API error responses")
public class ApiErrorResponse {
    @Schema(description = "Timestamp when the error occurred", example = "2026-09-02T21:20:01")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP status error description", example = "Bad Request")
    private String error;

    @Schema(description = "Detailed error message", example = "Product name is required")
    private String message;

    @Schema(description = "API endpoint path where the error was triggered", example = "/api/v1/products")
    private String path;
}