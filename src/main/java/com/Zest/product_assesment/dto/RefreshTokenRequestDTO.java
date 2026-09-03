package com.Zest.product_assesment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payload containing the refresh token for generating a new access token")
public class RefreshTokenRequestDTO {
    @Schema(description = "Active refresh token string", example = "d3b07384-d113-4988-9d8b-...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;
}