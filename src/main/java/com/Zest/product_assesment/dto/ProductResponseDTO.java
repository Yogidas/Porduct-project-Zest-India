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
@Schema(description = "Response payload representing product details")
public class ProductResponseDTO {
    @Schema(description = "Unique identifier of the product", example = "1")
    private Long id;

    @Schema(description = "Name of the product", example = "Wireless Mechanical Keyboard")
    private String productName;

    @Schema(description = "Available stock quantity", example = "25")
    private Integer quantity;

    @Schema(description = "Product price", example = "79.99")
    private Double price;

    @Schema(description = "User who created the product record", example = "admin_yogi")
    private String createdBy;

    @Schema(description = "Timestamp when the product was created", example = "2026-09-02T21:20:01")
    private LocalDateTime createdOn;

    @Schema(description = "User who last modified the product record", example = "admin_yogi")
    private String modifiedBy;

    @Schema(description = "Timestamp when the product was last modified", example = "2026-09-02T21:20:01")
    private LocalDateTime modifiedOn;
}