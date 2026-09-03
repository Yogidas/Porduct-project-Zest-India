package com.Zest.product_assesment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Payload for creating or updating a product")
public class ProductRequestDTO {
    @Schema(description = "Name of the product", example = "Wireless Mechanical Keyboard", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name is required")
    private String productName;

    @Schema(description = "Available inventory quantity", example = "25", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Schema(description = "Price of the product per unit", example = "79.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;
}