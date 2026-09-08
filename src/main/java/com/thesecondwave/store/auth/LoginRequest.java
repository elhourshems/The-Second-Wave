package com.thesecondwave.store.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "LoginRequest", description = "Credentials used to authenticate a user")
public class LoginRequest {
    @Schema(description = "Email address of the user", example = "kelly.slater@example.com")
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Schema(description = "Password of the user", example = "SurfsUp123")
    @NotBlank(message = "Password is required")
    private String password;
}
