package com.Zest.product_assesment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response payload containing authentication tokens")
public class JwtResponseDTO {
    @Schema(description = "JWT Access Token used for securing API requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Refresh Token used to generate a new access token upon expiration", example = "d3b07384-d113-4988-9d8b-...")
    private String refreshToken;
}