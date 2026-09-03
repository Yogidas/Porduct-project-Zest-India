package com.Zest.product_assesment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payload required for user authentication/login")
public class AuthRequestDTO {
    @Schema(description = "Username of the user", example = "yogi_admin")
    private String username;

    @Schema(description = "Password of the user", example = "securePassword123")
    private String password;
}