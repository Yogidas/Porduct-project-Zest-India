package com.Zest.product_assesment.dto;

import com.Zest.product_assesment.Entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Payload required for registering a new user")
public class RegisterRequestDTO {
    @Schema(description = "Unique username for login", example = "yogi_user", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Schema(description = "Password for the new account", example = "secretPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Schema(description = "Assigned user role (defaults to USER if omitted)", example = "USER")
    private Role role;
}